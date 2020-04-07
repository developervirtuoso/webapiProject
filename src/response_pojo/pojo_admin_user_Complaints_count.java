package response_pojo;

public class pojo_admin_user_Complaints_count {

	private String total_count = "0";
	private String pending_count = "0";
	private String process_count = "0";
	private String completed_count = "0";

	private String depot_name ="";
	public String getTotal_count() {
		return total_count;
	}
	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}
	public String getPending_count() {
		return pending_count;
	}
	public void setPending_count(String pending_count) {
		this.pending_count = pending_count;
	}
	public String getProcess_count() {
		return process_count;
	}
	public void setProcess_count(String process_count) {
		this.process_count = process_count;
	}
	public String getCompleted_count() {
		return completed_count;
	}
	public void setCompleted_count(String completed_count) {
		this.completed_count = completed_count;
	}

	public String getDepot_name() {
		return depot_name;
	}
	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}
	
	
}
