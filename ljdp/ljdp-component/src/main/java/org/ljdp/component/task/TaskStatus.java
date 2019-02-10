package org.ljdp.component.task;

public class TaskStatus {
	public static final String WAIT = "wait";
	public static final String PREPARED = "prepared";
	public static final String RUNNING = "running";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	
	public static int getSort(String state) {
		if(state.equals(RUNNING)) {
			return 1;
		} else if(state.equals(PREPARED)) {
			return 2;
		} else if(state.equals(WAIT)) {
			return 3;
		} else if(state.equals(SUCCESS)) {
			return 4;
		} else if(state.equals(FAILURE)) {
			return 4;
		}
		return 0;
	}
	
	public static int compare(String state1, String state2) {
		return getSort(state1) - getSort(state2);
	}
	
	public static boolean isFinish(String state) {
		if(state.equals(SUCCESS) || state.equals(FAILURE)) {
			return true;
		}
		return false;
	}
	
	public static String translation(String state) {
		if(state.equals(RUNNING)) {
			return "正在运行";
		} else if(state.equals(PREPARED)) {
			return "准备中";
		} else if(state.equals(WAIT)) {
			return "等待";
		} else if(state.equals(SUCCESS)) {
			return "完成（成功）";
		} else if(state.equals(FAILURE)) {
			return "完成（失败）";
		}
		return state;
	}
}
