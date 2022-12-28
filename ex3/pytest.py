# ----------------------------------------------------------
# Before You Run:
#
#   - "cd" to the MAKEFILE's directory 
#
#   - For every file 'f.txt' in the ./input/ directory, 
#     create 'f_Expected_Output.txt' in ./expected_output/ 
#
# ----------------------------------------------------------

import os

def main():
    for test in os.listdir("./InputsFromPDF/"):
        # run current test
        print("running %s : " % test)
        os.system("java -jar COMPILER InputsFromPDF/%s OutputsFromPDF/%s_Output.txt  >  /dev/null" % (test, test[:-4]))
        
        # find difference in outputs
        os.system("diff OutputsFromPDF/%s_Output.txt ExpectedOutputsFromPDF/%s_Expected_Output.txt > tmp.txt" % (test[:-4], test[:-4]))
        with open("tmp.txt") as tmp_file:
            diff_res = tmp_file.read()
        os.system("rm tmp.txt")

        # print result of current test
        if len(diff_res) == 0:
            print("\ttest passed! :)\n")
        else:
            print("\ttest did not pass.....\n")



if __name__ == "__main__":
    main()