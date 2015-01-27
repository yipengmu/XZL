package net.xinzeling.ui.myxzl;

import java.util.ArrayList;

import net.xinzeling.model.GuaModel.Gua;

public class HistoryGuaBean {

	ArrayList<Gua> mGuaInADay;

	public String getTime() {
		return mGuaInADay != null ? mGuaInADay.get(0).time : "";
	}
}
