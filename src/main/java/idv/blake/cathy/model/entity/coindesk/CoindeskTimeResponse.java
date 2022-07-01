package idv.blake.cathy.model.entity.coindesk;

public class CoindeskTimeResponse {
	private String updated;
	private String updatedISO;
	private String updateduk;

	// Getter Methods

	public String getUpdated() {
		return updated;
	}

	public String getUpdatedISO() {
		return updatedISO;
	}

	public String getUpdateduk() {
		return updateduk;
	}

	// Setter Methods

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public void setUpdatedISO(String updatedISO) {
		this.updatedISO = updatedISO;
	}

	public void setUpdateduk(String updateduk) {
		this.updateduk = updateduk;
	}
}
