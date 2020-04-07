package ke.co.ximmoz.fleet.views;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ke.co.ximmoz.fleet.R;

public class FileChooserFragFragment extends Fragment {

    private FileChooserFragViewModel mViewModel;

    public static FileChooserFragFragment newInstance() {
        return new FileChooserFragFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_chooser_frag_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FileChooserFragViewModel.class);
        // TODO: Use the ViewModel
    }

}