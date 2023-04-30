package ca.ubc.ece.resess;

/*******************************************************************************
 * Copyright (c) 2012 Secure Software Engineering Group at EC SPRIDE.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors: Christian Fritz, Steven Arzt, Siegfried Rasthofer, Eric
 * Bodden, and others.
 ******************************************************************************/

// yw: this file is modified upon original FlowDroid test cases, with copyright above 


import java.util.LinkedList;
import java.util.List;

import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.options.Options;

public class ConfigForTest implements IInfoflowConfig {

	@Override
	public void setSootOptions(Options options, InfoflowConfiguration config) {
		
		// explicitly include packages for shorter runtime:
		List<String> includeList = new LinkedList<String>();
		includeList.add("java.lang.*");
		includeList.add("java.util.*");
		includeList.add("java.io.*");
		includeList.add("sun.misc.*");
		includeList.add("java.net.*");
		includeList.add("javax.servlet.*");
		includeList.add("javax.crypto.*");
		includeList.add("javax.naming.*"); // for jndi 
		includeList.add("android.*");
		includeList.add("org.apache.http.*");
		includeList.add("de.test.*");
		includeList.add("soot.*");
		includeList.add("com.example.*");
		includeList.add("libcore.icu.*");

		// additional soot configs  
		Options.v().set_no_bodies_for_excluded(true);
		Options.v().set_allow_phantom_refs(true);
		options.set_include(includeList);
		options.set_output_format(Options.output_format_none);
		// options.set_src_prec(1); // "1" tells Flowdroid to accepts classes  
		// options.set_src_prec(3); // "3" tells Flowdroid to accepts Jimple 
		Options.v().setPhaseOption("jb", "use-original-names:true");
		// Options.v().setPhaseOption("cg.spark", "string-constants:true");
		Options.v().set_ignore_classpath_errors(true);
	}

}

