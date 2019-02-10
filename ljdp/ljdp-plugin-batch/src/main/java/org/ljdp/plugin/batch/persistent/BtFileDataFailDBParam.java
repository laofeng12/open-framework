package org.ljdp.plugin.batch.persistent;

import org.ljdp.core.db.DBQueryParam;

public class BtFileDataFailDBParam extends DBQueryParam {
	private String _eq_taskId;
	private String _like_dataItem;
	
	public String get_eq_taskId() {
		return _eq_taskId;
	}
	public void set_eq_taskId(String _eq_taskId) {
		this._eq_taskId = _eq_taskId;
	}
	public String get_like_dataItem() {
		return _like_dataItem;
	}
	public void set_like_dataItem(String _like_dataItem) {
		this._like_dataItem = _like_dataItem;
	}
}
