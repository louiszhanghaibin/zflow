package com.louisz.zflow.prcxmlcfg;

import java.util.ArrayList;
import java.util.List;

public interface NodeCfg {

	String name = "";

	String type = "";

	TransitionCfg transition = new TransitionCfg();

	String getName();

	TransitionCfg getTransition();

	List<TransitionCfg> transitions = new ArrayList<>();

	public List<TransitionCfg> getTransitions();

	public String getType();

	public RepeatCfg getRepeat();

	public List<ConditionCfg> getConditions();

}
