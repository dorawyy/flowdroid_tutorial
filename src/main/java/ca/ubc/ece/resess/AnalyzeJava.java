package ca.ubc.ece.resess;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import soot.jimple.infoflow.IInfoflow;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.taintWrappers.EasyTaintWrapper;

public class AnalyzeJava {
    protected static String appPath, libPath;

	protected static List<String> sinks;
	protected static List<String> sources;

    // specify sources and sinks

    public static void main(String[] args) throws IOException {
		
        // reset soot and setup config 
		resetSoot();
		setUp();

		// compute infoflow 
		IInfoflow infoflow = initInfoflow(false);
		List<String> epoints = new ArrayList<String>();
		// epoints.add("<ca.ubc.ece.resess.App: void main(java.lang.String[])>");
		epoints.add("<com.piggymetrics.account.controller.AccountController: com.piggymetrics.account.domain.Account createNewAccount(com.piggymetrics.account.domain.User)>");
		epoints.add("<com.piggymetrics.account.service.AccountServiceImpl: com.piggymetrics.account.domain.Account create(com.piggymetrics.account.domain.User)>");
		infoflow.computeInfoflow(appPath, libPath, epoints, sources, sinks);
        
		// print info flow results
		// checkInfoflow(infoflow);
		InfoflowResults map = infoflow.getResults();
		map.printResults();
	}


    
	protected static IInfoflow initInfoflow(boolean useTaintWrapper) {
		Infoflow result = new Infoflow("", false, null);
		result.setThrowExceptions(true);
		ConfigForTest testConfig = new ConfigForTest();
		result.setSootConfig(testConfig);
		if (useTaintWrapper) {
			EasyTaintWrapper easyWrapper;
			try {
				easyWrapper = EasyTaintWrapper.getDefault();
				result.setTaintWrapper(easyWrapper);
			} catch (IOException e) {
				System.err.println("Could not initialized Taintwrapper:");
				e.printStackTrace();
			}
		}
		return result;
	}


    /***
     * to set up soot config and specify the program to analyze
     * @throws IOException
     */
    public static void setUp() throws IOException {
		File f = new File(".");
		File testSrc = new File(f, "fileToAnalyze" + File.separator 
						+ // "log4j2shelldemo-1.0-SNAPSHOT-jar-with-dependencies.jar");
						"account-service-1.0-SNAPSHOT.jar");
		System.out.println("testSrc = " + testSrc.toString());
		
		if (!testSrc.exists()) {
			System.out.println("Test aborted - test source is not available");
			return;
		}

		StringBuilder appPathBuilder = new StringBuilder();
		appendWithSeparator(appPathBuilder, testSrc);
		
		appPath = appPathBuilder.toString();
		System.out.println("appPath = " + appPath);
		
		libPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar";
		System.out.println("libPath = " + libPath);

		sources = new ArrayList<String>();
		sources.add("<org.apache.logging.log4j.Logger: void error(java.lang.String)>");
		sources.add("<com.piggymetrics.account.domain.User: java.lang.String getUserName()>");

		sinks = new ArrayList<String>();
		sinks.add("<javax.naming.InitialContext: java.lang.Object lookup(java.lang.String)>");
		sinks.add("<javax.naming.InitialContext: java.lang.Object lookup(javax.naming.Name)>");
        sinks.add("<com.piggymetrics.account.repository.AccountRepository: com.piggymetrics.account.domain.Account save(com.piggymetrics.account.domain.Account)>");

		System.out.println("sources are:" + sources);
		System.out.println("sinks are:" + sinks);
	}



    public static void resetSoot() throws IOException {
		soot.G.reset();
		System.gc();

	}


    /**
	 * Appends the given path to the given {@link StringBuilder} if it exists
	 * 
	 * @param sb The {@link StringBuilder} to which to append the path
	 * @param f  The path to append
	 * @throws IOException
	 */
	private static void appendWithSeparator(StringBuilder sb, File f) throws IOException {
		if (f.exists()) {
			if (sb.length() > 0)
				sb.append(System.getProperty("path.separator"));
			sb.append(f.getCanonicalPath());
		}
	}
}
