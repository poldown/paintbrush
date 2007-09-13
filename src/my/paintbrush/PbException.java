package my.paintbrush;


public class PbException extends RuntimeException {

	private static final long serialVersionUID = 8117397144461769635L;
	
	/**
	 * The underlying throwable that caused the problem,
	 * or null if this information is not available.
	 */
	public Throwable throwable;
	
	public PbException (String message) {
		super(message);
	}
	
	/**
	 * Returns the underlying throwable that caused the problem,
	 * or null if this information is not available.
	 * <p>
	 * NOTE: This method overrides Throwable.getCause() that was
	 * added to JDK1.4. It is necessary to override this method
	 * in order for inherited printStackTrace() methods to work.
	 * </p>
	 * @return the underlying throwable
	 * 
	 * @since 3.1
	 */
	public Throwable getCause() {
		return throwable;
	}
	
	/**
	 *  Returns the string describing this SWTException object.
	 *  <p>
	 *  It is combined with the message string of the Throwable
	 *  which caused this SWTException (if this information is available).
	 *  </p>
	 *  @return the error message string of this SWTException object
	 */
	public String getMessage () {
		if (throwable == null) return super.getMessage ();
		return super.getMessage () + " (" + throwable.toString () + ")";
	}
	
	/**
	 * Outputs a printable representation of this exception's
	 * stack trace on the standard error stream.
	 * <p>
	 * Note: printStackTrace(PrintStream) and printStackTrace(PrintWriter)
	 * are not provided in order to maintain compatibility with CLDC.
	 * </p>
	 */
	public void printStackTrace () {
		super.printStackTrace ();
		if (throwable != null) {
			System.err.println ("*** Stack trace of contained exception ***"); //$NON-NLS-1$
			throwable.printStackTrace ();
		}
	}
}
