/**
 * 
 */
package myCreo;

import java.io.Serializable;

/**
 * @author derekok
 *
 */
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String employeeName;
	private String employeePassword;
	private String employeePosition;
	private String employeePermission;
	
	public User(String employeeName, String employeePassword,String employeePosition, String employeePermission)
	{
		this.employeeName = employeeName;
		this.employeePassword = employeePassword;
		this.employeePosition = employeePosition;
		this.employeePermission = employeePermission;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return the employeePassword
	 */
	public String getEmployeePassword() {
		return employeePassword;
	}
	/**
	 * @param employeePassword the employeePassword to set
	 */
	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}
	/**
	 * @return the employeePosition
	 */
	public String getEmployeePosition() {
		return employeePosition;
	}
	/**
	 * @param employeePosition the employeePosition to set
	 */
	public void setEmployeePosition(String employeePosition) {
		this.employeePosition = employeePosition;
	}
	@Override
	public String toString() {
		return "Employee Name=" + employeeName + ", Employee Password="
				+ employeePassword + ", Employee Position=" + employeePosition + "Employee Permissions=" + employeePermission;
	}
	/**
	 * @return the employeePermission
	 */
	public String getEmployeePermission() {
		return employeePermission;
	}
	/**
	 * @param employeePermission the employeePermission to set
	 */
	public void setEmployeePermission(String employeePermission) {
		this.employeePermission = employeePermission;
	}

}
