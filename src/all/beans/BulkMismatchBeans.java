package all.beans;

public class BulkMismatchBeans {
	private String id;
	private String date;
	private String file;
	private String type;
	private String response_msg;
	private String process;
	private int run_status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResponse_msg() {
		return response_msg;
	}
	public void setResponse_msg(String response_msg) {
		this.response_msg = response_msg;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public int getRun_status() {
		return run_status;
	}
	public void setRun_status(int run_status) {
		this.run_status = run_status;
	}
	@Override
	public String toString() {
		return "BulkMismatchBeans [id=" + id + ", date=" + date + ", file=" + file + ", type=" + type
				+ ", response_msg=" + response_msg + ", process=" + process + ", run_status=" + run_status + "]";
	}
	
	
	
}
