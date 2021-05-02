package com.company.company;

import com.company.beans.Employee;

import com.company.beans.Job;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


import java.sql.Date;
import java.util.Arrays;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CompanyApplicationTests {

	private static final String BASE_URL = "http://localhost:8080/api/employees";
	private static final String ADD = BASE_URL +"/add";
	private static final String ALL = BASE_URL +"/all";
	private final RestTemplate restTemplate = new RestTemplate();

//	@Test
//	public void contextLoads() {
//
//	}

	@Test
	public void testAddEmployees() {

		// create two employees
		String name1 = "Name " + System.currentTimeMillis();
		String name2 = "Name " + System.currentTimeMillis();

		Employee expectedEmp1 = new Employee(name1, 22_000);
		Employee expectedEmp2 = new Employee(name2, 33_000);

		// add jobs to expectedEmp1 only
		List<Job> jobs = Arrays.asList(new Job(name1+"_job1", new Date(System.currentTimeMillis()+10)),
				new Job(name1+"_job2", new Date(System.currentTimeMillis()+10)));

		expectedEmp1.setJobs(jobs);

		Long emp1Id = restTemplate.postForObject(ADD, expectedEmp1, Long.class);
		Long emp2Id = restTemplate.postForObject(ADD, expectedEmp2, Long.class);

		// get actual employees from DB
		Employee actualEmp1 = restTemplate.getForObject(BASE_URL+"/"+emp1Id, Employee.class);
		Employee actualEmp2 = restTemplate.getForObject(BASE_URL+"/"+emp2Id, Employee.class);

		// do some validation
		Assert.assertEquals(expectedEmp1.getName(), actualEmp1.getName());
		Assert.assertEquals(expectedEmp1.getSalary(), actualEmp1.getSalary(), 0.000001);
		Assert.assertEquals(expectedEmp1.getJobs().size(), jobs.size());
		Assert.assertEquals(expectedEmp2.getName(), actualEmp2.getName());
		Assert.assertEquals(expectedEmp2.getSalary(), actualEmp2.getSalary(), 0.000001);

		// print
		System.out.println(expectedEmp1);
		System.out.println(expectedEmp2);

		// print all
		Employee[] employees = restTemplate.getForObject(ALL, Employee[].class);
		Arrays.stream(employees).forEach(System.out::println);
	}

}
