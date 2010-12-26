// ***** This file is automatically generated from OneOf.java.jpp

package daikon.inv.unary.sequence;

import daikon.*;
import daikon.inv.*;

  import daikon.inv.unary.scalar.*;
  import daikon.inv.binary.twoSequence.SubSequence;

import utilMDE.*;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.*;

// This subsumes an "exact" invariant that says the value is always exactly
// a specific value.  Do I want to make that a separate invariant
// nonetheless?  Probably not, as this will simplify implication and such.

  /**
   * Represents sequences of double values where the elements of the sequence
   * take on only a few distinct values.  Prints as either
   * <samp>x[] == c</samp> (when there is only one value), or as
   * <samp>x[] one of {c1, c2, c3}</samp> (when there are multiple values).
   */

public final class EltOneOfFloat
  extends SingleFloatSequence
  implements OneOf
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20030822L;

  /**
   * Debugging logger.
   **/
  public static final Logger debug
    = Logger.getLogger (EltOneOfFloat.class.getName());

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  /**
   * Boolean.  True iff OneOf invariants should be considered.
   **/
  public static boolean dkconfig_enabled = true;

  /**
   * Positive integer.  Specifies the maximum set size for this type
   * of invariant (x is one of <code>size</code> items).
   **/

  public static int dkconfig_size = 3;

  // Probably needs to keep its own list of the values, and number of each seen.
  // (That depends on the slice; maybe not until the slice is cleared out.
  // But so few values is cheap, so this is quite fine for now and long-term.)

  private double[] elts;
  private int num_elts;

  public EltOneOfFloat (PptSlice slice) {
    super (slice);
    if (slice == null)
      return;

    Assert.assertTrue(var().type.isPseudoArray(),
                  "ProglangType must be pseudo-array for EltOneOfFloat");

    elts = new double[dkconfig_size];

    num_elts = 0;
  }

  private static EltOneOfFloat proto;

  /** Returns the prototype invariant for EltOneOfFloat **/
  public static Invariant get_proto() {
    if (proto == null)
      proto = new EltOneOfFloat (null);
    return (proto);
  }

  /** returns whether or not this invariant is enabled **/
  public boolean enabled() {
    return dkconfig_enabled;
  }

  /** instantiate an invariant on the specified slice **/
  public Invariant instantiate_dyn (PptSlice slice) {
    return new EltOneOfFloat(slice);
  }

  public boolean is_boolean() {
    return (var().file_rep_type.elementType() == ProglangType.BOOLEAN);
  }
  public boolean is_hashcode() {
    return (var().file_rep_type.elementType() == ProglangType.HASHCODE);
  }

  public EltOneOfFloat clone() {
    EltOneOfFloat result = (EltOneOfFloat) super.clone();
    result.elts = elts.clone();

    result.num_elts = this.num_elts;
    return result;
  }

  public int num_elts() {
    return num_elts;
  }

  public Object elt() {
    return elt(0);
  }

  public Object elt(int index) {
    if (num_elts <= index)
      throw new Error("Represents " + num_elts + " elements, index " + index + " not valid");

    return Intern.internedDouble(elts[index]);
  }

  private void sort_rep() {
    Arrays.sort(elts, 0, num_elts );
  }

  public double min_elt() {
    if (num_elts == 0)
      throw new Error("Represents no elements");
    sort_rep();
    return elts[0];
  }

  public double max_elt() {
    if (num_elts == 0)
      throw new Error("Represents no elements");
    sort_rep();
    return elts[num_elts-1];
  }

  // Assumes the other array is already sorted
  public boolean compare_rep(int num_other_elts, double[] other_elts) {
    if (num_elts != num_other_elts)
      return false;
    sort_rep();
    for (int i=0; i < num_elts; i++)
      if (! (Global.fuzzy.eq(elts[i], other_elts[i]) || (Double.isNaN(elts[i]) && Double.isNaN(other_elts[i])))) // elements are interned
        return false;
    return true;
  }

  private String subarray_rep() {
    // Not so efficient an implementation, but simple;
    // and how often will we need to print this anyway?
    sort_rep();
    StringBuffer sb = new StringBuffer();
    sb.append("{ ");
    for (int i=0; i<num_elts; i++) {
      if (i != 0)
        sb.append(", ");

      if (PrintInvariants.dkconfig_static_const_infer) {
        boolean curVarMatch = false;
        PptTopLevel pptt = ppt.parent;
        for (VarInfo vi : pptt.var_infos) {
          if (vi.is_static_constant && VarComparability.comparable(vi, var())) {
            // If variable is a double, then use fuzzy comparison
            if (vi.rep_type == ProglangType.DOUBLE) {
              Double constantVal = (Double)vi.constantValue();
              if (Global.fuzzy.eq(constantVal, elts[i]) || (Double.isNaN(constantVal) && Double.isNaN(elts[i]))) {
                sb.append(vi.name());
                curVarMatch = true;
              }
            }
            // Otherwise just use the equals method
            else {
              Object constantVal = vi.constantValue();
              if (constantVal.equals(elts[i])) {
                sb.append(vi.name());
                curVarMatch = true;
              }
            }
          }
        }

        if (curVarMatch == false) {
          sb.append((Double.isNaN(elts[i]) ? "Double.NaN" : String.valueOf(elts[i])));
        }
      }
      else {
        sb.append((Double.isNaN(elts[i]) ? "Double.NaN" : String.valueOf(elts[i])));
      }

    }
    sb.append(" }");
    return sb.toString();
  }

  public String repr() {
    return "EltOneOfFloat" + varNames() + ": "
      + "falsified=" + falsified
      + ", num_elts=" + num_elts
      + ", elts=" + subarray_rep();
  }

  public String format_using(OutputFormat format) {
    sort_rep();

    if (format.isJavaFamily()) return format_java_family(format);

    if (format == OutputFormat.DAIKON) {
      return format_daikon();
    } else if (format == OutputFormat.IOA) {
      return format_ioa();
    } else if (format == OutputFormat.SIMPLIFY) {
      return format_simplify();
    } else if (format == OutputFormat.ESCJAVA) {
      return format_esc();
    } else {
      return format_unimplemented(format);
    }
  }

  public String format_daikon() {
    String varname = var().name() + " elements";
    if (num_elts == 1) {

        if (PrintInvariants.dkconfig_static_const_infer) {
          PptTopLevel pptt = ppt.parent;
          for (VarInfo vi : pptt.var_infos) {
            if (vi.is_static_constant && VarComparability.comparable(vi, var())) {
              if (vi.rep_type == ProglangType.DOUBLE) {
                Double constantVal = (Double)vi.constantValue();
                if (Global.fuzzy.eq(constantVal, elts[0]) || (Double.isNaN(constantVal) && Double.isNaN(elts[0]))) {
                  return varname + " == " + vi.name();
                }
              }
              else {
                Object constantVal = vi.constantValue();
                if (constantVal.equals(elts[0])) {
                  return varname + " == " + vi.name();
                }
              }
            }
          }
        }
        return varname + " == " + (Double.isNaN(elts[0]) ? "Double.NaN" : String.valueOf(elts[0]));
    } else {
      return varname + " one of " + subarray_rep();
    }
  }

  /* IOA */
  public String format_ioa() {
    sort_rep();

    Quantify.IOAQuantification quant = VarInfo.get_ioa_quantify (var());
    String varname = quant.getFreeVarName(0);

    String result;

    {
      result = "";
      for (int i=0; i<num_elts; i++) {
        if (i != 0) { result += " \\/ "; }
        if (PrintInvariants.dkconfig_static_const_infer) {
          boolean curVarMatch = false;
          PptTopLevel pptt = ppt.parent;
          for (VarInfo vi : pptt.var_infos) {
            if (vi.is_static_constant && VarComparability.comparable(vi, var())) {
              if (vi.rep_type == ProglangType.DOUBLE) {
                Double constantVal = (Double)vi.constantValue();
                if (Global.fuzzy.eq(constantVal, elts[i]) || (Double.isNaN(constantVal) && Double.isNaN(elts[i]))) {
                  result += "(" + varname + " = " + vi.name() + ")";
                  curVarMatch = true;
                }
              }
              else {
                Object constantVal = vi.constantValue();
                if (constantVal.equals(elts[i])) {
                  result += "(" + varname + " = " + vi.name() + ")";
                  curVarMatch = true;
                }
              }
            }
          }
          if (curVarMatch == false)  {
            result += "(" + varname + " = " + (Double.isNaN(elts[i]) ? "Double.NaN" : String.valueOf(elts[i])) + ")";
          }
        }
        else {
          result += "(" + varname + " = " + (Double.isNaN(elts[i]) ? "Double.NaN" : String.valueOf(elts[i])) + ")";
        }
      }
    }

    result = quant.getQuantifierExp() + quant.getMembershipRestriction(0) + " => " + result + quant.getClosingExp();

    return result;
  }

  public String format_esc() {
    sort_rep();

    String[] form = VarInfo.esc_quantify (var());
    String varname = form[1];

    String result;

    {
      result = "";
      for (int i=0; i<num_elts; i++) {
        if (i != 0) { result += " || "; }
        result += varname + " == " + (Double.isNaN(elts[i]) ? "Double.NaN" : String.valueOf(elts[i]));
      }
    }

    result = form[0] + "(" + result + ")" + form[2];

    return result;
  }

  public String format_java_family(OutputFormat format) {

    String result;

    // Setting up the name of the unary variable
    String varname = var().name_using(format);

    // Constructing the array that unary val will be compared against

    String oneOfArray = "new double[] { ";

    for (int i = 0 ; i < num_elts ; i++) {
      if (i != 0) { oneOfArray += ", "; }
      oneOfArray = oneOfArray + (Double.isNaN(elts[i]) ? "Double.NaN" : String.valueOf(elts[i]));
    }
    oneOfArray += " }";

    // Calling quantification method
    if (num_elts == 1) {

        {
          result = "daikon.Quant.eltsEqual(" + varname + ", "
            + (Double.isNaN(elts[0]) ? "Double.NaN" : String.valueOf(elts[0])) + ")";
        }
    } else {
      Assert.assertTrue(num_elts > 1);
      // eltsOneOf == subsetOf
      result = "daikon.Quant.subsetOf(" + varname + ", " + oneOfArray + ")";
    }

    return result;
  }

  public String format_simplify() {

    sort_rep();

    String[] form = VarInfo.simplify_quantify (var());
    String varname = form[1];

    String result;

    {
      result = "";
      for (int i=0; i<num_elts; i++) {
        result += " (EQ " + varname + " " + simplify_format_double(elts[i]) + ")";
      }
      if (num_elts > 1) {
        result = "(OR" + result + ")";
      } else if (num_elts == 1) {
        // chop leading space
        result = result.substring(1);
      } else {
        // Haven't actually seen any data, so we're vacuously true
        return format_too_few_samples(OutputFormat.SIMPLIFY, null);
      }
    }

    result = form[0] + result + form[2];

    if (result.indexOf("format_simplify") == -1)
      daikon.simplify.SimpUtil.assert_well_formed(result);
    return result;
  }

  /* Old version with interleaved "ifdefs", replaced with following version
     using separate ELT and non-elt routines with a shared subroutine

  public InvariantStatus add_modified(double[] a, int count) {
    InvariantStatus status = InvariantStatus.NO_CHANGE;
  OUTER:
   for (int ai=0; ai<a.length; ai++) {
    double v = a[ai];

    for (int i=0; i<num_elts; i++) {
      //if (logDetail())
      //  log ("add_modified (" + v + ")");
      if ((Global.fuzzy.eq(elts[i], v) || (Double.isNaN(elts[i]) && Double.isNaN( v)))) {

        continue OUTER;

      }
    }
    if (num_elts == dkconfig_size) {
      if (logOn() || debug.isLoggable(Level.FINE))
        log (debug, "destroy of '" + format() + "' add_modified (" + v + ")");
      destroyAndFlow();
      return;
    }

    // We are significantly changing our state (not just zeroing in on
    // a constant), so we have to flow a copy before we do so.  We even
    // need to clone if this has 0 elements becuase otherwise, lower
    // ppts will get versions of this with multiple elements once this is
    // expanded.
    cloneAndFlow();

    elts[num_elts] = v;
    num_elts++;
    status = InvariantStatus.WEAKENED;

   }

  return status;
  }

  */

  public InvariantStatus add_modified(double[] a, int count) {
    return runValue(a, count, true);
  }

  public InvariantStatus check_modified(double[] a, int count) {
    return runValue(a, count, false);
  }

  private InvariantStatus runValue(double[] a, int count, boolean mutate) {
    InvariantStatus finalStatus = InvariantStatus.NO_CHANGE;
    for (int ai=0; ai <a.length; ai++) {
      InvariantStatus status = null;
      if (mutate) {
        status = add_mod_elem(a[ai], count);
      } else {
        status = check_mod_elem(a[ai], count);
      }
      if (status == InvariantStatus.FALSIFIED) {
        return InvariantStatus.FALSIFIED;
      } else if (status == InvariantStatus.WEAKENED) {
        finalStatus = InvariantStatus.WEAKENED;
      }
    }
    return finalStatus;
  }

  /**
   * Adds a single sample to the invariant.  Returns
   * the appropriate InvariantStatus from the result
   * of adding the sample to this.
   */
  public InvariantStatus add_mod_elem (double v, int count) {
    InvariantStatus status = check_mod_elem(v, count);
    if (status == InvariantStatus.WEAKENED) {
      elts[num_elts] = v;
      num_elts++;
    }
    return status;
  }

  /**
   * Checks a single sample to the invariant.  Returns
   * the appropriate InvariantStatus from the result
   * of adding the sample to this.
   */
  public InvariantStatus check_mod_elem (double v, int count) {

    // Look for v in our list of previously seen values.  If it's
    // found, we're all set.
    for (int i=0; i<num_elts; i++) {
      //if (logDetail())
      //  log ("add_modified (" + v + ")");
      if ((Global.fuzzy.eq(elts[i], v) || (Double.isNaN(elts[i]) && Double.isNaN( v)))) {
        return (InvariantStatus.NO_CHANGE);
      }
    }

    if (num_elts == dkconfig_size) {
      return (InvariantStatus.FALSIFIED);
    }

    return (InvariantStatus.WEAKENED);
  }

  // It is possible to have seen many (array) samples, but no (double)
  // array element values.
  public boolean enoughSamples() {
    return num_elts > 0;
  }

  protected double computeConfidence() {
    // This is not ideal.
    if (num_elts == 0) {
      return Invariant.CONFIDENCE_UNJUSTIFIED;
    } else {
      return Invariant.CONFIDENCE_JUSTIFIED;
    }
  }

  public DiscardInfo isObviousStatically(VarInfo[] vis) {
    // Static constants are necessarily OneOf precisely one value.
    // This removes static constants from the output, which might not be
    // desirable if the user doesn't know their actual value.
    if (vis[0].isStaticConstant()) {
      Assert.assertTrue(num_elts <= 1);
      return new DiscardInfo(this, DiscardCode.obvious, vis[0].name() + " is a static constant.");
    }
    return super.isObviousStatically(vis);
  }

  public DiscardInfo isObviousDynamically(VarInfo[] vis) {
    DiscardInfo super_result = super.isObviousDynamically(vis);
    if (super_result != null) {
      return super_result;
    }

    VarInfo v = vis[0];
    // Look for the same property over a supersequence of this one.
    PptTopLevel pptt = ppt.parent;
    for (Iterator<Invariant> inv_itor = pptt.invariants_iterator(); inv_itor.hasNext(); ) {
      Invariant inv = inv_itor.next();
      if (inv == this) {
        continue;
      }
      if (inv instanceof EltOneOfFloat) {
        EltOneOfFloat other = (EltOneOfFloat) inv;
        if (isSameFormula(other)
          && SubSequence.isObviousSubSequenceDynamically(this, v, other.var())) {
          debug.fine ("isObviousDyn: Returning true because isObviousSubSequenceDynamically");
          return new DiscardInfo(this, DiscardCode.obvious, "The same property holds over a supersequence " + other.var().name());
        }
      }
    }

    return null;
  }

  /**
   * Oneof can merge different formulas from lower points to create a single
   * formula at an upper point.
   */
  public boolean mergeFormulasOk() {
    return (true);
  }

  public boolean isSameFormula(Invariant o) {
    EltOneOfFloat other = (EltOneOfFloat) o;
    if (num_elts != other.num_elts)
      return false;
    if (num_elts == 0 && other.num_elts == 0)
      return true;

    sort_rep();
    other.sort_rep();

    for (int i=0; i < num_elts; i++) {
      if (! (Global.fuzzy.eq(elts[i], other.elts[i]) || (Double.isNaN(elts[i]) && Double.isNaN(other.elts[i]))))
        return false;
    }

    return true;
  }

  public boolean isExclusiveFormula(Invariant o) {
    if (o instanceof EltOneOfFloat) {
      EltOneOfFloat other = (EltOneOfFloat) o;

      if (num_elts == 0 || other.num_elts == 0)
        return false;
      for (int i=0; i < num_elts; i++) {
        for (int j=0; j < other.num_elts; j++) {
          if ((Global.fuzzy.eq(elts[i], other.elts[j]) || (Double.isNaN(elts[i]) && Double.isNaN(other.elts[j])))) // elements are interned
            return false;
        }
      }

      return true;
    }

    // Many more checks can be added here:  against nonzero, modulus, etc.
    if ((o instanceof EltNonZeroFloat) && (num_elts == 1) && (elts[0] == 0)) {
      return true;
    }
    double elts_min = Double.MAX_VALUE;
    double elts_max = Double.MIN_VALUE;
    for (int i=0; i < num_elts; i++) {
      elts_min = Math.min(elts_min, elts[i]);
      elts_max = Math.max(elts_max, elts[i]);
    }
    if ((o instanceof LowerBoundFloat) && (elts_max < ((LowerBoundFloat)o).min()))
      return true;
    if ((o instanceof UpperBoundFloat) && (elts_min > ((UpperBoundFloat)o).max()))
      return true;

    return false;
  }

  // OneOf invariants that indicate a small set of possible values are
  // uninteresting.  OneOf invariants that indicate exactly one value
  // are interesting.
  public boolean isInteresting() {
    if (num_elts() > 1) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasUninterestingConstant() {

    for (int i = 0; i < num_elts; i++) {
      if (elts[i] < -1.0 || elts[i] > 2.0 || elts[i] != (long)elts[i])
        return true;
    }

    return false;
  }

  public boolean isExact() {
    return (num_elts == 1);
  }

  // Look up a previously instantiated invariant.
  public static EltOneOfFloat find(PptSlice ppt) {
    Assert.assertTrue(ppt.arity() == 1);
    for (Invariant inv : ppt.invs) {
      if (inv instanceof EltOneOfFloat)
        return (EltOneOfFloat) inv;
    }
    return null;
  }

  // Interning is lost when an object is serialized and deserialized.
  // Manually re-intern any interned fields upon deserialization.
  private void readObject(ObjectInputStream in) throws IOException,
    ClassNotFoundException {
    in.defaultReadObject();

    for (int i=0; i < num_elts; i++)
      elts[i] = Intern.intern(elts[i]);
  }

  /**
   * Merge the invariants in invs to form a new invariant.  Each must be
   * a EltOneOfFloat invariant.  This code finds all of the oneof values
   * from each of the invariants and returns the merged invariant (if any).
   *
   * @param invs       List of invariants to merge.  The invariants must all be
   *                   of the same type and should come from the children of
   *                   parent_ppt.  They should also all be permuted to match
   *                   the variable order in parent_ppt.
   * @param parent_ppt Slice that will contain the new invariant
   */
  public Invariant merge (List<Invariant> invs, PptSlice parent_ppt) {

    // Create the initial parent invariant from the first child
    EltOneOfFloat  first = (EltOneOfFloat) invs.get(0);
    EltOneOfFloat result = first.clone();
    result.ppt = parent_ppt;

    // Loop through the rest of the child invariants
    for (int i = 1; i < invs.size(); i++ ) {

      // Get this invariant
      EltOneOfFloat inv = (EltOneOfFloat) invs.get (i);

      // Loop through each distinct value found in this child and add
      // it to the parent.  If the invariant is falsified, there is no parent
      // invariant
      for (int j = 0; j < inv.num_elts; j++) {
        double val = inv.elts[j];

        InvariantStatus status = result.add_mod_elem(val, 1);
        if (status == InvariantStatus.FALSIFIED) {
          result.log ("child value '" + val + "' destroyed oneof");
          return (null);
        }
      }
    }

    result.log ("Merged '" + result.format() + "' from " + invs.size()
                + " child invariants");
    return (result);
  }

  /**
   * Setup the invariant with the specified elements.  Normally
   * used when searching for a specified OneOf
   */
  public void set_one_of_val (double[] vals) {

    num_elts = vals.length;
    for (int i = 0; i < num_elts; i++)
      elts[i] = Intern.intern (vals[i]);
  }

  /**
   * Returns true if every element in this invariant is contained in
   * the specified state.  For example if x = 1 and the state contains
   * 1 and 2, true will be returned.
   */
  public boolean state_match (Object state) {

    if (num_elts == 0)
      return (false);

    if (!(state instanceof double[]))
      System.out.println ("state is of class '" + state.getClass().getName()
                          + "'");
    double[] e = (double[]) state;
    for (int i = 0; i < num_elts; i++) {
      boolean match = false;
      for (int j = 0; j < e.length; j++) {
        if (elts[i] == e[j]) {
          match = true;
          break;
        }
      }
      if (!match)
        return (false);
    }
    return (true);
  }

}
