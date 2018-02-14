package ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Loan;

/**
 * Servlet implementation class Start
 */
@WebServlet({"/Start", "/Startup", "/Startup/*"})
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public String startPage = "/UI.jspx";
    public String resultsPage = "/Result.jspx";
    
    private static final String MONTHLYPAY = "monthlyPayments";
    private static final String GRACEINTEREST = "graceInterest";
    private static final String FIXED_INTEREST = "fixedInterest";
    private static final String GRACE_PERIOD = "gracePeriod";
    private static final String MODEL = "model";
    private static final String ERRORMESSAGE = "errorMessage";
    
    private static final String PRINCIPAL = "principal";
    private static final String INTEREST = "interest";
    private static final String PERIOD = "period";
    private static final String GRACE_ENABLED = "graceEnabled";
    
    private static final String MAX_PRINCIPAL = "maxPrincipal";
    
    private Loan loan;
    
    private String principal, interest, period, gracePeriodEnabled;
    
    private double monthlyPayments, graceInterest;
    
    private boolean errorFlag = false;
    private String errorMessage;
	
    /**
     * It is awkward to intermix html and java code because it is hard to see the
     * structure of the html when you are outputting it in java as well as violation
     * of the separation principal.
     */
    
    /**
     * Mixing validation, payment computation and presentation violates the separation
     * of concern principal since the calculation of values should be separated from
     * the validation of user input and presentation of input as seen in the MVC design
     * pattern. When mixing these three it not only creates uglier code but opens the 
     * code up to bugs that can often be hard to fix when the code is not modularized.
     */
    
    /**
     * The content-length measures the size of the data in the body of the request/response
     */
    
    /**
     * GET should be used when only information from the server needs to be retrieved
     * with no side effects while POST should be used when information needs to be 
     * preserved and side effects are present.
     */
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Start() {
        super();
    }
    
    public void init() throws ServletException {
    		loan = new Loan();
    		//getServletContext().setAttribute(MODEL, loan);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().setAttribute("legendName", getServletContext().getInitParameter("legendName"));
		
		errorMessage = null;
		//submit was not pressed, so we forward to the start page
		if (request.getParameter("submit") == null) {
			request.getRequestDispatcher(startPage).forward(request, response);
		}
		else {	//submit has been pressed, compute the result
			computePayment(request);
			if (errorMessage == null) {
				request.getRequestDispatcher(resultsPage).forward(request, response);
			}
			else {
				request.getRequestDispatcher(startPage).forward(request, response);
			}
		}
		//The below code is to generate an exception to test the exception page
		//int[] ex = {1, 2, 3};
		//int genException = ex[5];
	}

	public void updateJSP(HttpServletRequest request) {
		//save the session attributes in case the user presses reset
		request.getSession().setAttribute(PRINCIPAL, principal);
		request.getSession().setAttribute(INTEREST, interest);
		request.getSession().setAttribute(PERIOD, period);
		request.getSession().setAttribute(GRACE_ENABLED, gracePeriodEnabled);
		
		request.getSession().setAttribute(ERRORMESSAGE, errorMessage);
		
		DecimalFormat d = new DecimalFormat("##.0");
		//System.out.println(graceInterest);
		getServletContext().setAttribute(GRACEINTEREST, graceInterest);
		getServletContext().setAttribute(MONTHLYPAY, d.format(monthlyPayments));
	}
	
	public void computePayment(HttpServletRequest request) {
		String fixedInterest, gracePeriod;
		
		principal = request.getParameter(PRINCIPAL);
		period = request.getParameter(PERIOD);
		interest = request.getParameter(INTEREST);
		gracePeriodEnabled = request.getParameter(GRACE_ENABLED);
		
		//System.out.println("gracePeriodEnabled = " + gracePeriodEnabled);
		
		fixedInterest = getServletContext().getInitParameter(FIXED_INTEREST);
		gracePeriod = getServletContext().getInitParameter(GRACE_PERIOD);
		
		try {
			monthlyPayments = loan.computePayment(principal, period, interest, gracePeriodEnabled, gracePeriod, fixedInterest);
			graceInterest = loan.computeGraceInterest(principal, gracePeriod, interest, fixedInterest, gracePeriodEnabled);
		} 
		catch (NumberFormatException e) {		//if any fields are left blank
			errorMessage = "All fields are required!";
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error has been generated by computePayment!");
			errorMessage = "All parameters must be positive!";
		}
		
		updateJSP(request);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
