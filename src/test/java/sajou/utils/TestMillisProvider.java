package sajou.utils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils.MillisProvider;
import org.joda.time.Period;


public class TestMillisProvider implements MillisProvider {

	private DateTime now = DateTime.now();
	
	@Override
	public long getMillis() {
		return now.getMillis();
	}
	
	public void offset(final int millis) {
		now = now.plusMillis(millis);
	}
	
	public void offset(final Period period) {
		now = now.plus(period);
	}
}
