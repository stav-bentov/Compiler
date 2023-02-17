"""
    Usage: `$ python3 tester.py` from project's directory.
    First, Make sure:
        - The relevant compiler is located in `./COMPILER`, the tests located in
        - The relevant tests are located in `./input` as `txt` files.
        - Each test has a corresponding `Expected_Output` file in dir `./expected_output`.
    Then, the script outputs:
        - All the stdout-s of the compilation of the tests in `./logs`.
        - All the tests outputs (mips sources and semantic checks includes) in `./output`.
        - If no assertion was raised during the script run, all tested have passed :)
"""

import subprocess
import os

# ---- CONFIG ----
input_dir = "./input/"
output_dir = "./output/"
expected_output_dir = "./expected_output/"
logs_dir = "./logs/"
flag_save_logs = True     # saves the stdout of each run to `logs_dir`
comparison_only = False   # skips running the compiler and saving the outputs


def list_test_files_by_dir():
    global test_files
    test_files = os.listdir(input_dir)


def run_on_test_files():
    # assumes all files in 'input_files' located in path 'input/'
    for i, filename in enumerate(test_files):
        input_file =  input_dir + filename                                          # input/test.txt
        sem_output_file = output_dir + filename[:-4] + "_Semantic_Output.txt"       # output/test_SEMANTIC_OUTPUT.txt
        mips_source_output_file = output_dir + filename[:-4] + "_MIPS_SOURCE.txt"   # output/test_MIPS_SOURCE.txt
        mips_run_output_file = output_dir + filename[:-4] + "_MIPS_OUTPUT.txt"      # output/test_MIPS_OUTPUT.txt

        print("---- #" + str(i + 1) + " / " + str(len(test_files)) + " | Running: " + filename + " ----")
        result = subprocess.run(['java', '-jar', 'COMPILER', input_file, mips_source_output_file],
                                capture_output=True, text=True)
        save_log(filename, result.stdout)
        assert result.stderr == '', "Got error while running: \n"+ result.stderr

        os.rename(src=output_dir + "semantic_status.txt", dst=sem_output_file)
        with open(sem_output_file) as f:
            assert f.read().strip() == "OK" , "Got lexing/parsing/semantic error. L syntax must be obeyed!"

        with open(mips_run_output_file, 'w') as f:
            result = subprocess.run(['spim', '-f', mips_source_output_file],
                                    text=True, stdout=f)
            assert result.returncode == 0, "Got Error while compiling the generated mips \n" + result.stderr


def simple_tester():
    # compares outputs to expected ones, IGNORING starting/ending whitespaces
    print("\n\n=============== SIMPLE_TESTER ================")

    for i, filename in enumerate(test_files):
        print("---- #" + str(i + 1) + " / " + str(len(test_files)) + " | Checking: " + filename + " ----")
        input_file =  input_dir + filename                                             # input/test.txt
        output_file = output_dir + filename[:-4] + "_MIPS_OUTPUT.txt"                  # output/test_MIPS_OUTPUT.txt
        expected_file = expected_output_dir + filename[:-4] + "_EXPECTED_OUTPUT.txt"   # expected_output/test_EXPECTED_OUTPUT.txt
        with open(output_file, 'r') as f1, open(expected_file, 'r') as f2:
            s1 = f1.read().strip().split('\n',maxsplit=5)[5:]  # split & slicing will remove mips' annoying prefix
            s2 = f2.read().strip().split('\n',maxsplit=5)[5:]
            s1, s2 = ''.join(s1), ''.join(s2)

            assert s1 == s2, "\n---> Test failed on: " + filename +\
                             "\n---> Got: " + s1 + "   Expected: " + s2 +\
                             "\n---> Or rerun : java -jar COMPILER " + input_file + " " + output_file

    print("\n============= SUCCESS - SIMPLE_TESTER ==============")


# ------- Logs: ---------
def init_logs_dir():
    subprocess.run(['rm', '-r', logs_dir])
    subprocess.run(['mkdir', logs_dir])


def save_log(_fname, _stdout):
    if flag_save_logs:
        log_file = logs_dir + _fname[:-4] + "_log_run.txt"
        with open(log_file, "w") as f:
            f.write(str(_stdout))
        print(f"[Saved log to:{log_file}]")




if __name__ == '__main__':
    # lists test files located in input_dir
    list_test_files_by_dir()

    if not comparison_only:
        # init `dir to save logs
        init_logs_dir()

        # runs the compiler on all files
        run_on_test_files()

    # validate the result from last step
    simple_tester()