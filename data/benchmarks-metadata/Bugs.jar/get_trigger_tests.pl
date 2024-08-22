#!/usr/bin/env perl
# ------------------------------------------------------------------------------
#
# This script collects the list of trigger test cases originally reported by the
# benchmark metadata for a given bug and prints the name of each test to the
# provided output file. The name of a trigger test case follows the following
# format:
#   --- <test class name>[::<test method name>]
# E.g.,
#   --- org.foo.TestFoo::testBar
#
# Usage:
# ./get_trigger_tests.pl
#   <Bugs.jar json file>
#   <Bugs.jar test-results.txt file>
#   <output file, e.g., trigger_tests.txt>
#
# ------------------------------------------------------------------------------

use strict;
use warnings;

use JSON::MaybeXS qw(decode_json);
use List::MoreUtils qw(uniq);

$#ARGV == 2 or die("Usage: get_trigger_tests.pl <Bugs.jar json file> <Bugs.jar test-results.txt file> <output file, e.g., trigger_tests.txt>");

# Inputs
my $JSON_FILE          = $ARGV[0];
my $TEST_LOG_FILE      = $ARGV[1];
# Output
my $TRIGGER_TESTS_FILE = $ARGV[2];

-e $JSON_FILE or die("$JSON_FILE does not exist!");
-e $TEST_LOG_FILE or die("$TEST_LOG_FILE does not exist!");

my @trigger_tests = ();

#
# Parse json file
#

open(IN, "<$JSON_FILE") or die("Cannot read $JSON_FILE");
  my @json_input = <IN>;
close(IN);

my $data = decode_json(join('', @json_input));
my $failing_test_classes = $data->{failing_tests};

# Sanity check, is there any failing test class?
scalar(@{$failing_test_classes}) > 0 or die("There are not any failing test class in $JSON_FILE!");

#
# Parse test log file and collect all test cases that failed
#

my @test_log_lines = `egrep -a "<<< ERROR|<<< FAILURE" $TEST_LOG_FILE | grep -v "Tests run:"`;
for my $line(@test_log_lines) {
  my $test_class_name  = "";
  my $test_method_name = "";

  # Parameterized test method: <method>[<param>](<class>)  Time elapsed...
  if ($line =~ /^([^\[]*)\[([^\]]*)\]\(([^)]*)\)  Time elapsed.*$/) {
    $test_class_name  = $3;
    $test_method_name = $1;
  # Test method: <method>(<class>)  Time elapsed...
  } elsif ($line =~ /^([^(]*)\(([^)]*)\)  Time elapsed.*$/) {
    $test_class_name  = $2;
    $test_method_name = $1;
  # Test class: <class>  Time elapsed...
  } elsif ($line =~ /^([^(]*)  Time elapsed.*$/) {
    $test_class_name  = $1;
    $test_method_name = "";
  } else {
    die("Unexpected line in test log ($TEST_LOG_FILE): >>>$line<<<!");
  }

  # Sanity check, is $test_class_name an expected failing test class?
  grep(/^$test_class_name$/, @{$failing_test_classes}) or warn("Unexpected failing test class $test_class_name!");

  if ($test_method_name eq "") {
    push @trigger_tests, "$test_class_name";
  } else {
    push @trigger_tests, "${test_class_name}::${test_method_name}";
  }
}

#
# Additionally, for each failing test class reported in the <Bugs.jar json file>
# collect all its failing test cases, if they have not been already collected in
# the previous step.
#

@test_log_lines = `tac $TEST_LOG_FILE`;
for my $failing_test_class (@{$failing_test_classes}) {
  if ($failing_test_class eq "") {
    # Skip empty entries, e.g.,
    # https://github.com/jose/apr_eval_framework/blob/master/data/benchmarks-metadata/Bugs.jar/Logging-Log4J2/3b2e880e/3b2e880e.json#L9
    next;
  }
  if (grep(/.*\/Camel\/7b1253db\/7b1253db.json$/, $JSON_FILE) and $failing_test_class eq "CamelReceivedTimestamp") {
    # Skip invalid entries, e.g.,
    # https://github.com/jose/apr_eval_framework/blob/master/data/benchmarks-metadata/Bugs.jar/Camel/7b1253db/7b1253db.json#L11
    next;
  }
  if (grep(/^$failing_test_class$/, @trigger_tests) or grep(/^${failing_test_class}::.*$/, @trigger_tests)) {
    # Skip it, if it has been reported
    next;
  }
  for my $line(@test_log_lines) {
    if ($line =~ /^Failed tests:\s*(.*)\($failing_test_class\)/) {
      push @trigger_tests, "${failing_test_class}::$1";
    } elsif ($line =~ /^\s*(.*)\[([^\]]*)\]\($failing_test_class\)/) {
      # Parameterized test method
      push @trigger_tests, "${failing_test_class}::$1";
    } elsif ($line =~ /^\s*(.*)\($failing_test_class\)/) {
      # No-parameterized test method
      push @trigger_tests, "${failing_test_class}::$1";
    } elsif ($line =~ /^Running\s*$failing_test_class$/) {
      # Fix for
      # Logging-Log4J2/2afe3dff
      # Logging-Log4J2/3b12e13d
      # Logging-Log4J2/7f391872
      # Logging-Log4J2/88641f49
      # Logging-Log4J2/c79a743b
      # Logging-Log4J2/d8af1c93
      #
      # but before reporting a failing test class, check if no test case of it
      # has been reported.  if so, ignore the failing test class.
      if (grep(/^$failing_test_class$/, @trigger_tests) or grep(/^${failing_test_class}::.*$/, @trigger_tests)) {
        next;
      }
      push @trigger_tests, "$failing_test_class";
    }
  }
}

# Sanity check, is there any failing test cases?
scalar(@trigger_tests) > 0 or die("No failing test cases has been identified in $TEST_LOG_FILE!");

# Print a sorted list of trigger tests to the output file
open(my $fh, ">$TRIGGER_TESTS_FILE") or die("Could not open $TRIGGER_TESTS_FILE to write the list of trigger tests!");
for my $trigger_test (sort(uniq(@trigger_tests))) {
  print $fh "--- $trigger_test\n";
}
close($fh);
