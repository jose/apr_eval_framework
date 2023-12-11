# Automatic Program Repair Evaluation (APR-Eval) Framework

APR-Eval is a framework that allows the execution of automatic program repair
tools on reproducible benchmarks of defects by providing an abstraction around
the repair tools and the defects in those benchmarks.

If you use the APR-Eval framework, please cite the paper that describes it:
```bibtex
<!-- TODO -->
```

## Support

Currently, APR-Eval supports 11 Automatic Program Repair (APR), 5 defect benchmarks,
and 1,761 reproducible defects.  The <!-- TODO --> file lists all defects that
are reproducible under Java 8.

| #  | Tool          | Language | Repository                          | Commit id |
| -- | ------------- | -------- | ----------------------------------- | --------- |
| 1  | Nopol         | Java     | https://github.com/SpoonLabs/nopol  | 7ba58a78d |
| 2  | DynaMoth      | Java     | https://github.com/SpoonLabs/nopol  | 7ba58a78d |
| 3  | NPEFix        | Java     | https://github.com/SpoonLabs/npefix | 403445b9a |
| 4  | jGenProg      | Java     | https://github.com/SpoonLabs/Astor  | 26ee3dfc8 |
| 5  | jKali         | Java     | https://github.com/SpoonLabs/Astor  | 26ee3dfc8 |
| 6  | jMutRepair    | Java     | https://github.com/SpoonLabs/Astor  | 26ee3dfc8 |
| 7  | Cardumen      | Java     | https://github.com/SpoonLabs/Astor  | 26ee3dfc8 |
| 8  | ARJA          | Java     | https://github.com/yyxhdy/arja      | c34e400ed |
| 9  | GenProg-A     | Java     | https://github.com/yyxhdy/arja      | c34e400ed |
| 10 | RSRepair-A    | Java     | https://github.com/yyxhdy/arja      | c34e400ed |
| 11 | Kali-A        | Java     | https://github.com/yyxhdy/arja      | c34e400ed |

| # | Benchmark      | Language | # Projects | # Defects | # Reproducible Defects | Link                                           |
| - | -------------- | -------- | ----------:| ---------:| ----------------------:| ---------------------------------------------  |
| 1 | Bears          | Java     |         72 |       251 |                    ??? | https://github.com/bears-bugs/bears-benchmark  |
| 2 | Bugs.jar       | Java     |          8 |     1,158 |                    ??? | https://github.com/bugs-dot-jar/bugs-dot-jar   | 
| 3 | Defects4J      | Java     |          6 |       395 |                    ??? | https://github.com/rjust/defects4j             |
| 4 | IntroClassJava | Java     |          6 |       297 |                    ??? | https://github.com/Spirals-Team/IntroClassJava |
| 5 | QuixBugs       | Java     |         40 |        40 |                    ??? | https://github.com/jkoppel/QuixBugs            |
|   | **Total**      |          |        132 |     2,141 |                    ??? |                                                |

## Setting up APR-Eval

### Requirements

* Linux (wget, time)
* [Perl](https://www.perl.org/) >= 5.0.10 and [cpan](https://www.cpan.org)
* [Apache Maven](https://maven.apache.org)
* [Apache Ant](https://ant.apache.org)
* [Gradle](https://gradle.org)
* [Git](https://git-scm.com) >= 1.9 and [SVN](https://subversion.apache.org) >= 1.8

### Steps to set up APR-Eval

1. Clone this repository with

```bash
git clone --recursive https://github.com/jose/apr_eval_framework.git
```

2. Initialize the framework with [`./init.sh`](init.sh).  The script configures
Python 2, Java 8, initialize all modules and submodules of this repository, get
a copy of maven dependencies for maven-based projects, and configures Z3.

3. Check installation <!-- TODO: command -->.

## Using APR-Eval

APR-Eval requires Python 2.  That is, one must first run the following

```bash
source "env/bin/activate"
```

Use `python script/repair.py` to run the repair tools on the benchmarks

Command line

```bash
python script/repair.py {Arja,GenProg,Kali,RSRepair,jKali,jGenProg,jMutRepair,Cardumen,Nopol,DynaMoth,NPEFix}
    --benchmark {Bears, Bugs.jar, Defects4J, IntroClassJava, QuixBugs}
    --id <bug_id> # optional, if not specified all the bugs of the benchmark will be used.
```

Example:

```bash
python script/repair.py Nopol --benchmark Defects4J --id Chart-5
```

```
├── benchmark name
│ ├── project
│ │ ├── bug id
│ │ │ ├── tool
│ │ │ │ ├── random seed
│ │ │ │ │ ├── repair.log (stdout from the repair tool)
│ │ │ │ │ ├── result.json (see below)
│ │ │ │ │ ├── grid5k.stderr.log (on Grid5k)
│ │ │ │ │ └── detailed-result.json (available only for some repair tool)
```

The `result.json` file is structured as follows:

```javascript
{
  "repair_begin": timestamp of the beginning of the repair tool execution, 
  "repair_end": timestamp of the end of the repair tool execution, 
  "patches": [
    {
      "patch": textual representation of the diff between the buggy source code and the patched source code
      // other information depending on the repair tool
    }
  ]
}
```

## Repository structure

The directory structure of APR-Eval is as follows:

```
├── benchmarks: contains a git submodule per benchmark plugged-in APR-Eval
├── data:
│ ├── benchmarks: contains additional information/files on benchmarks
│ ├── benchmarks-metadata: contains additional metadata on defects
│ ├── benchmarks-reproducibility: contains additional information on the reproducibility of each defect
│ ├── repair_tools: contains information on repair tools, e.g. the launcher class name
├── libs: contains the dependencies
├── repair_tools: contains a jar file per repair tool (e.g., npefix.jar) or per repair framework where several repair tools are implemented (e.g., astor.jar)
├── script: contains the actual scripts of APR-Eval that use everything mentioned above to run repair tools on benchmarks of defects
```

## Extending APR-Eval

### New tool

1. Add the binary (jar file) of your repair tool in `./repair_tools`.

2. Create a new file in `script/core/repair_tools/` that contains the following content:

```py
import os
import subprocess
import datetime
import json
import shutil

from config import OUTPUT_PATH
from config import WORKING_DIRECTORY
from core.RepairTool import RepairTool
from core.utils import add_repair_tool
from core.runner.RepairTask import RepairTask

class <repair_tool_name>(RepairTool):
    """<repair_tool_name>"""

    def __init__(self, name="<repair_tool_name>"):
        super(<repair_tool_name>, self).__init__(name, "<repair_tool_name>")
        self.seed = 0
        self.iteration = iteration

    def repair(self, repair_task):
        """"
        :type repair_task: RepairTask
        """
        bug = repair_task.bug
        bug_path = os.path.join(WORKING_DIRECTORY,
                                "%s_%s_%s_%s" % (self.name, bug.benchmark.name, bug.project, bug.bug_id))
        repair_task.working_directory = bug_path
        self.init_bug(bug, bug_path)

        try:
            failing_tests =  bug.failing_tests(),
            sources = bug.source_folders(),
            tests = bug.test_folders(),
            bin_folders = bug.bin_folders(),
            test_bin_folders = bug.test_bin_folders(),
            classpath = bug.classpath(),
            compliance_level = bug.compliance_level(),

            # run the repair tool
        finally:
            result = {
                "repair_begin": self.repair_begin,
                "repair_end": datetime.datetime.now().__str__(),
                "patches": []
            }
            repair_task.status = "FINISHED"
            
            # normalize the output in result
            with open(os.path.join(repair_task.log_dir(), "result.json"), "w+") as fd2:
                json.dump(result, fd2, indent=2)
            shutil.rmtree(bug_path)
        pass

def init(args):
    return <repair_tool_name>()

def _args(parser):
    # additional argument for the repair tool
    parser.add_argument("--argument", help="description", default=100)
    pass

parser = add_repair_tool("<repair_tool_name>", init, 'Repair the bug with <repair_tool_name>')
_args(parser)
```

3. Go to `script/core/utils.py` and import your repair tool in the end of the file (like `import core.repair_tools.<repair_tool_name>`).

4. Add the file `<repair_tool_name>.json` into folder `data/repair_tools/`.
The file must have the following content:

```
{
	"name": "<repair_tool_name>",
	"version": "x.y.z",
	"jar": "<repair_tool_name>.jar",
	"main": "<main of the class containing the main (the entry point of the tool)>"
}

```

### Upgrade existing benchmark

<!-- TODO -->

### New benchmark

<!-- TODO -->

## License

MIT License, see [`LICENSE`](LICENSE) for more information.
