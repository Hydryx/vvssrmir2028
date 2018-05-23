package agenda.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import agenda.exceptions.InvalidFormatException;
import agenda.model.base.Activity;
import agenda.model.base.Contact;
import agenda.model.repository.classes.RepositoryActivityFile;
import agenda.model.repository.classes.RepositoryContactFile;
import agenda.model.repository.classes.RepositoryContactMock;
import agenda.model.repository.interfaces.RepositoryActivity;
import agenda.model.repository.interfaces.RepositoryContact;

import org.junit.Before;
import org.junit.Test;

public class TestClass {

	RepositoryActivity rep;
	private Activity act;
	private Contact con;
	private RepositoryContact repc;
	private RepositoryActivity repAct;
	private RepositoryContact repCon;

	@Before
	public void setUp() throws Exception {
		RepositoryContact repcon = new RepositoryContactFile();
		rep = new RepositoryActivityFile(repcon);
		repc = new RepositoryContactMock();
		repCon = new RepositoryContactFile();
		repAct = new RepositoryActivityFile(repCon);

		for (Activity a : repAct.getActivities())
			repAct.removeActivity(a);
	}

	@Test
	public void testCase1() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		try {
			act = new Activity("name1", 
					df.parse("03/20/2013 12:00"), 
					df.parse("03/20/2013 13:00"),
					null,
					"Lunch break");
			rep.addActivity(act);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(1 == rep.count());
	}

	@Test
	public void testCase2() {
		try {
			con = new Contact("name", "address1", "+4071122334455");
		} catch (InvalidFormatException e) {
			assertTrue(false);
		}
		//int n = rep.count();
		repc.addContact(con);
		for(Contact c : repc.getContacts())
			if (c.equals(con))
			{
				assertTrue(true);
				break;
			}
		//assertTrue(n+1 == rep.count());
	}
	
	@Test
	public void testCase3() {
		for (Activity act : rep.getActivities())
			rep.removeActivity(act);

		Calendar c = Calendar.getInstance();
		c.set(2013, 3 - 1, 20, 12, 00);
		Date start = c.getTime();

		c.set(2013, 3 - 1, 20, 12, 30);
		Date end = c.getTime();

		Activity act = new Activity("name1", start, end,
				new LinkedList<Contact>(), "description2");

		rep.addActivity(act);

		c.set(2013, 3 - 1, 20);

		List<Activity> result = rep.activitiesOnDate("name1", c.getTime());
		assertTrue(result.size() == 1);
	}
	
	@Test
	public void testCase4() {
		boolean part1 = false, part2 = false, part3 = false;
		int n = repCon.count();

		try {
			Contact c = new Contact("name", "address1", "+071122334455");
			repCon.addContact(c);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

		if (n + 1 == repCon.count())
			part1 = true;
		Activity act = null;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		try {
			act = new Activity("name1", df.parse("03/20/2013 12:00"),
					df.parse("03/20/2013 13:00"), null, "Lunch break");
			repAct.addActivity(act);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (repAct.getActivities().get(0).equals(act) && repAct.count() == 1)
			part2 = true;

		try {
			repAct.activitiesOnDate("name1", (Date) (Object) "ASD");
		} catch (Exception e) {
			part3 = true;
		}
		assertTrue(part1 && part2 && part3);
	}
	
	@Test
	public void testCase5() {
	
	}
}
