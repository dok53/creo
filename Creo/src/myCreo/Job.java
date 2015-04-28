/**
 * 
 */
package myCreo;

import java.io.Serializable;

/**
 * @author derekok
 *
 */
public class Job implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jobName;
	private String jobAddress;
	private String jobSurname;
	private String jobLandline;
	private String jobMobile;
	private String jobCounty;
	private String jobEmail;
	private String jobCustomerType;
	private String jobProductType;
	private String jobReferral;
	private String jobSource;
	private String jobLiveClosed;
	private String jobPassedTo;
	private String jobComments;
	private String primaryRecord;
	private String status;
	private String closingReason;
	private String rep;
	private String sale;
	private String commission;
	private String receptionist;

	public Job(String jobName,String surname, String jobAddress,String jobLandline,String jobMobile, 
			String jobEmail, String jobCounty, String jobCustomerType, String jobProductType,
			String jobReferral,String jobSource,String jobLiveClosed, String jobPassedTo,
			String jobComments, String primaryRecord, String status, String closingReason, String rep,String sale, String commission, String receptionist)
	{
		this.jobName = jobName;
		this.jobSurname = surname;
		this.jobAddress = jobAddress;
		this.jobLandline = jobLandline;
		this.jobMobile = jobMobile;
		this.jobCounty = jobCounty;
		this.jobEmail = jobEmail;
		this.jobCustomerType = jobCustomerType;
		this.jobProductType = jobProductType;
		this.jobReferral = jobReferral;
		this.jobSource = jobSource;
		this.jobLiveClosed = jobLiveClosed;
		this.jobPassedTo = jobPassedTo;
		this.jobComments = jobComments;
		this.primaryRecord = primaryRecord;
		this.status = status;
		this.closingReason = closingReason;
		this.rep = rep;
		this.sale = sale;
		this.commission = commission;
		this.receptionist = receptionist;
	}
	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}
	/**
	 * @param jobName the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	/**
	 * @return the jobSurname
	 */
	public String getJobSurname() {
		return jobSurname;
	}
	/**
	 * @param jobSurname the jobSurname to set
	 */
	public void setJobSurname(String jobSurname) {
		this.jobSurname = jobSurname;
	}
	/**
	 * @return the jobAddress
	 */
	public String getJobAddress() {
		return jobAddress;
	}
	/**
	 * @param jobAddress the jobAddress to set
	 */
	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}
	/**
	 * @return the jobCounty
	 */
	public String getJobCounty() {
		return jobCounty;
	}
	/**
	 * @param jobCounty the jobCounty to set
	 */
	public void setJobCounty(String jobCounty) {
		this.jobCounty = jobCounty;
	}
	/**
	 * @return the jobDescription
	 */
	public String getJobComments() {
		return jobComments;
	}
	/**
	 * @param jobDescription the jobDescription to set
	 */
	public void setJobComments(String jobComments) {
		this.jobComments = jobComments;
	}
	/**
	 * @return the jobMobile
	 */
	public String getJobMobile() {
		return jobMobile;
	}
	/**
	 * @param jobMobile the jobMobile to set
	 */
	public void setJobMobile(String jobMobile) {
		this.jobMobile = jobMobile;
	}
	/**
	 * @return the jobLandline
	 */
	public String getJobLandline() {
		return jobLandline;
	}
	/**
	 * @param jobLandline the jobLandline to set
	 */
	public void setJobLandline(String jobLandline) {
		this.jobLandline = jobLandline;
	}
	/**
	 * @return the jobEmail
	 */
	public String getJobEmail() {
		return jobEmail;
	}
	/**
	 * @param jobEmail the jobEmail to set
	 */
	public void setJobEmail(String jobEmail) {
		this.jobEmail = jobEmail;
	}
	/**
	 * @return the jobCustomerType
	 */
	public String getJobCustomerType() {
		return jobCustomerType;
	}
	/**
	 * @param jobCustomerType the jobCustomerType to set
	 */
	public void setJobCustomerType(String jobCustomerType) {
		this.jobCustomerType = jobCustomerType;
	}
	/**
	 * @return the jobReferral
	 */
	public String getJobReferral() {
		return jobReferral;
	}
	/**
	 * @param jobReferral the jobReferral to set
	 */
	public void setJobReferral(String jobReferral) {
		this.jobReferral = jobReferral;
	}
	/**
	 * @return the jobSource
	 */
	public String getJobSource() {
		return jobSource;
	}
	/**
	 * @param jobSource the jobSource to set
	 */
	public void setJobSource(String jobSource) {
		this.jobSource = jobSource;
	}
	/**
	 * @return the jobPassedTo
	 */
	public String getJobPassedTo() {
		return jobPassedTo;
	}
	/**
	 * @param jobPassedTo the jobPassedTo to set
	 */
	public void setJobPassedTo(String jobPassedTo) {
		this.jobPassedTo = jobPassedTo;
	}
	/**
	 * @return the jobProductType
	 */
	public String getJobProductType() {
		return jobProductType;
	}
	/**
	 * @param jobProductType the jobProductType to set
	 */
	public void setJobProductType(String jobProductType) {
		this.jobProductType = jobProductType;
	}
	/**
	 * @return the jobLiveClosed
	 */
	public String getJobLiveClosed() {
		return jobLiveClosed;
	}
	/**
	 * @param jobLiveClosed the jobLiveClosed to set
	 */
	public void setJobLiveClosed(String jobLiveClosed) {
		this.jobLiveClosed = jobLiveClosed;
	}
	@Override
	public String toString() {
		return "Job Name = " + jobName + ", Job Surname = " + jobSurname + ", Address = " + jobAddress
				+ ", Landline = " + jobLandline + ", Mobile = " + jobMobile 
				+ ", Email = " + jobEmail + ", County = " + jobCounty +"\n" 
				+ ", CustomerType = " + jobCustomerType + " Product = " + jobProductType + ", Referral = "
				+ jobReferral + ", Source = " + jobSource + ", PassedTo = "
				+ jobPassedTo + ", Comments = " + jobComments + "\n" + "Time " + primaryRecord;
	}
	public String getToString()
	{
		return toString();
	}
	/**
	 * @return the time
	 */
	public String getJobTimeAccepted() {
		return primaryRecord;
	}
	/**
	 * @param time the time to set
	 */
	public void setJobTimeAccepted(String time) {
		this.primaryRecord = time;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the closingReason
	 */
	public String getClosingReason() {
		return closingReason;
	}
	/**
	 * @param closingReason the closingReason to set
	 */
	public void setClosingReason(String closingReason) {
		this.closingReason = closingReason;
	}
	/**
	 * @return the rep
	 */
	public String getRep() {
		return rep;
	}
	/**
	 * @param rep the rep to set
	 */
	public void setRep(String rep) {
		this.rep = rep;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getReceptionist() {
		return receptionist;
	}
	public void setReceptionist(String receptionist) {
		this.receptionist = receptionist;
	}

}
