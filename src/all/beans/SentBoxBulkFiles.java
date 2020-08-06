package all.beans;

public class SentBoxBulkFiles {
	private String id;
	private String date;
	private String sent_file;
	private String get_file;
	private String process;
	private String status_msg;
	private String status;
	private String datetime;
	private String run_status;
	
	public String getRun_status() {
		return run_status;
	}
	public void setRun_status(String run_status) {
		this.run_status = run_status;
	}
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
	public String getSent_file() {
		return sent_file;
	}
	public void setSent_file(String sent_file) {
		this.sent_file = sent_file;
	}
	public String getGet_file() {
		return get_file;
	}
	public void setGet_file(String get_file) {
		this.get_file = get_file;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getStatus_msg() {
		return status_msg;
	}
	public void setStatus_msg(String status_msg) {
		this.status_msg = status_msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	@Override
	public String toString() {
		return "SentBoxBulkFiles [id=" + id + ", date=" + date + ", sent_file=" + sent_file + ", get_file=" + get_file
				+ ", process=" + process + ", status_msg=" + status_msg + ", status=" + status + ", datetime="
				+ datetime + "]";
	}
	
	
}
