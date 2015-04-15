package smartie_dissertation.accesscontrol.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdVersion;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.XACMLProperties;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithmFactory;
import com.att.research.xacmlatt.pdp.policy.PolicyDef;
import com.att.research.xacmlatt.pdp.policy.PolicyFinder;
import com.att.research.xacmlatt.pdp.policy.PolicySet;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;
import com.att.research.xacmlatt.pdp.policy.Target;
import com.att.research.xacmlatt.pdp.std.StdPolicyFinder;
import com.att.research.xacmlatt.pdp.std.StdPolicyFinderFactory;
import com.att.research.xacmlatt.pdp.util.ATTPDPProperties;

public class SmartieStdPolicyFinderFactory extends StdPolicyFinderFactory {
//	private Log logger							= LogFactory.getLog(this.getClass());
	private List<PolicyDef> rootPolicies;
	private List<PolicyDef> referencedPolicies;
	private boolean needsInit					= true;
	
	protected synchronized void init(Properties properties) {
		if (this.needsInit) {
			//
			// Check for property that combines root policies into one policyset
			//
			String combiningAlgorithm = properties.getProperty(ATTPDPProperties.PROP_POLICYFINDERFACTORY_COMBINEROOTPOLICIES);
			if (combiningAlgorithm != null) {
				try {
					MyLog.log("Combining root policies with " + combiningAlgorithm, MyLog.logMessageType.DEBUG, this.getClass().toString());
//					logger.info("Combining root policies with " + combiningAlgorithm);
					//
					// Find the combining algorithm
					//
					CombiningAlgorithm<PolicySetChild> algorithm = CombiningAlgorithmFactory.newInstance().getPolicyCombiningAlgorithm(new IdentifierImpl(combiningAlgorithm));
					//
					// Create our root policy
					//
					PolicySet root = new PolicySet();
					root.setIdentifier(new IdentifierImpl(UUID.randomUUID().toString()));
					root.setVersion(StdVersion.newInstance("1.0"));
					root.setTarget(new Target());
					//
					// Set the algorithm
					//
					root.setPolicyCombiningAlgorithm(algorithm);
					//
					// Load all our root policies
					//
//					for (PolicyDef policy : this.getPolicyDefs(XACMLProperties.PROP_ROOTPOLICIES, properties)) {
//						root.addChild(policy);
//					}
					for (PolicyDef policy : PRP.getPolicyDefs()) {
						root.addChild(policy);
					}
					//
					// Set this policy as the root
					//
					this.rootPolicies = new ArrayList<>();
					this.rootPolicies.add(root);
				} catch (FactoryException | ParseException e) {
					MyLog.log("Failed to load Combining Algorithm Factory: " + e.getLocalizedMessage(), MyLog.logMessageType.ERROR, this.getClass().toString());
//					logger.error("Failed to load Combining Algorithm Factory: " + e.getLocalizedMessage());
				}
			} else {
				this.rootPolicies		= PRP.getPolicyDefs();
			}			
			this.referencedPolicies	= PRP.getPolicyDefs();
			this.needsInit	= false;
		}
	}
	
	public SmartieStdPolicyFinderFactory() {
		super();
	}

	public SmartieStdPolicyFinderFactory(Properties properties) {
		super(properties);
	}

	@Override
	public PolicyFinder getPolicyFinder() throws FactoryException {
		try {
			this.init(XACMLProperties.getProperties());
		} catch (IOException e) {
			throw new FactoryException(e);
		}
		return new StdPolicyFinder(this.rootPolicies, this.referencedPolicies);
	}

	@Override
	public PolicyFinder getPolicyFinder(Properties properties) throws FactoryException {
		this.init(properties);
		return new StdPolicyFinder(this.rootPolicies, this.referencedPolicies, properties);
	}
		
}
