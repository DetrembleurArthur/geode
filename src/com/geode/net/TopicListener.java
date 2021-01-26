package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;

public interface TopicListener
{
	void trigger(ArrayList<Serializable> args);
}
