import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class TestLogger implements TestRule, MethodRule {

	public Statement apply(Statement arg0, Description arg1) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public Statement apply(Statement arg0, FrameworkMethod arg1, Object arg2) {
		// TODO Auto-generated method stub
		return arg0;
	}
}
