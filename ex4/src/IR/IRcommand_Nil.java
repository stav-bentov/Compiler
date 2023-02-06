/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_Nil extends IRcommand{
    TEMP t;

    public IRcommand_Nil(TEMP t) {
        this.t = t;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().li(t, 0);
    }
}
