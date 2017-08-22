package apextechies.etodo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apextechies.etodo.R;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 05 Aug 2017 at 9:35 AM
 */

public class ComingSoon extends Fragment {
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.comingsoon,container,false);
	}
}
