package com.poct.android.view.fragment;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.poct.R;
import com.poct.android.presenter.SettingPresenter;


public class AudioFragment extends BaseFragment {

    public SettingPresenter mSettingPresenter;
    public Switch switchButtonAudio;
    public SeekBar seekBar1;
    public SeekBar seekBar2;
    public AudioFragment() {

    }

    @SuppressLint("ValidFragment")
    public AudioFragment(SettingPresenter mSettingPresenter) {
        // Required empty public constructor
        this.mSettingPresenter = mSettingPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_audioset, container, false);
        switchButtonAudio = mView.findViewById(R.id.btn_swich);
        seekBar1 = mView.findViewById(R.id.seekbar_sys_audio_level);
        seekBar2 = mView.findViewById(R.id.seekbar_mus_audio_level);
        seekBar1.setMax(mSettingPresenter.mSettingActivity.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        seekBar1.setProgress(mSettingPresenter.mSettingActivity.mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
        seekBar2.setMax(mSettingPresenter.mSettingActivity.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar2.setProgress(mSettingPresenter.mSettingActivity.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBar1.setOnSeekBarChangeListener(seekBar1Listener);
        seekBar2.setOnSeekBarChangeListener(seekBar2Listener);
        try {
            int a = Settings.System.getInt(mSettingPresenter.mSettingActivity.getContentResolver(),Settings.System.SOUND_EFFECTS_ENABLED);
            if(a == 0)
            {
                switchButtonAudio.setChecked(false);
            }
            else
            {
                switchButtonAudio.setChecked(true);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        switchButtonAudio.setOnCheckedChangeListener(audioSwichListener);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    public CompoundButton.OnCheckedChangeListener audioSwichListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Settings.System.putInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                    Settings.System.SOUND_EFFECTS_ENABLED, isChecked ? 1 : 0);
            if(isChecked)
            {
                Settings.System.putInt(mSettingPresenter.mSettingActivity.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED,
                        1);
                mSettingPresenter.mSettingActivity.mAudioManager.loadSoundEffects();

            }
            else
            {
                Settings.System.putInt(mSettingPresenter.mSettingActivity.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED,
                        0);
                mSettingPresenter.mSettingActivity.mAudioManager.unloadSoundEffects();
            }
        }
    };

    public SeekBar.OnSeekBarChangeListener seekBar1Listener = new SeekBar.OnSeekBarChangeListener()
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSettingPresenter.mSettingActivity.mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,progress,AudioManager.FLAG_PLAY_SOUND);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public SeekBar.OnSeekBarChangeListener seekBar2Listener = new SeekBar.OnSeekBarChangeListener()
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSettingPresenter.mSettingActivity.mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,AudioManager.FLAG_PLAY_SOUND);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
