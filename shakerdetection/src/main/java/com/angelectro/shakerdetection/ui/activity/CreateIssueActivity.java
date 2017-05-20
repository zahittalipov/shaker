package com.angelectro.shakerdetection.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.exception.SlackUserNotSettingsException;
import com.angelectro.shakerdetection.model.InformationLog;
import com.angelectro.shakerdetection.ui.settingslack.activity.SettingsSlackActivity;
import com.angelectro.shakerdetection.utils.KeyboardUtils;
import com.angelectro.shakerdetection.utils.PrefUtils;
import com.angelectro.shakerdetection.utils.UiUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class CreateIssueActivity extends AppCompatActivity implements CreateIssueView {


    private static final int REQUEST_CODE_SLACK = 23;
    public static String INFO_BUNDLE = "info_bundle";
    private TextView mVersionCode;
    private TextView mVersionName;
    private EditText mAuthorComment;
    private EditText mTitle;
    private ImageView mImageView;
    private CheckBox mBoxSlack;
    private CheckBox mBoxJira;
    private InformationLog mInformationLog;
    private CreateIssuePresenter mPresenter;
    private MaterialDialog mMaterialDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        mPresenter = new CreateIssuePresenter(this);
        mPresenter.attachView(this);
        if (getActionBar() != null)
            getActionBar().show();
        if (getSupportActionBar() != null)
            getSupportActionBar().show();
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_issue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mInformationLog.setComment(mAuthorComment.getText().toString());
        mTitle.clearFocus();
        mAuthorComment.clearFocus();
        if (item.getItemId() == R.id.action_send) {
            if (mBoxJira.isChecked() && TextUtils.isEmpty(mTitle.getText())) {
                UiUtils.showToast(getString(R.string.enter_name_issue), getApplicationContext());
                KeyboardUtils.showKeyboard(mTitle);
            } else if (mBoxSlack.isChecked() || mBoxJira.isChecked())
                mPresenter.sendInfo(mInformationLog, mBoxSlack.isChecked(), mBoxJira.isChecked(), mTitle.getText().toString());
            else
                UiUtils.showToast(getString(R.string.nothing_not_selected), getApplicationContext());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void initView() {
        mVersionCode = (TextView) findViewById(R.id.version_code_tv);
        mVersionName = (TextView) findViewById(R.id.version_name_tv);
        mAuthorComment = (EditText) findViewById(R.id.et_author_comment);
        mTitle = (EditText) findViewById(R.id.et_title);
        mImageView = (ImageView) findViewById(R.id.screenshot_image);
        mBoxJira = (CheckBox) findViewById(R.id.box_jira);
        mBoxSlack = (CheckBox) findViewById(R.id.box_slack);
        mBoxSlack.setChecked(PrefUtils.Settings.isCheckedSlack(getApplicationContext()));
        boolean checkedJira = PrefUtils.Settings.isCheckedJira(getApplicationContext());
        mBoxJira.setChecked(checkedJira);
        mTitle.setVisibility(checkedJira ? VISIBLE : GONE);
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            int id = buttonView.getId();
            if (id == R.id.box_jira) {
                mTitle.setVisibility(isChecked ? VISIBLE : GONE);
            }
            mPresenter.saveSettings(mBoxSlack.isChecked(), mBoxJira.isChecked());
        };
        mBoxJira.setOnCheckedChangeListener(listener);
        mBoxSlack.setOnCheckedChangeListener(listener);
    }

    void initData() {
        mInformationLog = getIntent().getParcelableExtra(INFO_BUNDLE);
        mImageView.setImageDrawable(new BitmapDrawable(mInformationLog.getBitmap()));
        mVersionCode.setText(mInformationLog.getVersionCode());
        mVersionName.setText(mInformationLog.getVersionName());
    }

    @Override
    public void sendResult(Boolean ok) {
        if (ok) {
            Toast.makeText(this, R.string.message_sended, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showError(Throwable throwable) {
        if (throwable instanceof SlackUserNotSettingsException) {
            startActivityForResult(new Intent(this, SettingsSlackActivity.class), REQUEST_CODE_SLACK);
        } else
            throwable.printStackTrace();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SLACK) {
            mPresenter.sendInfo(mInformationLog, mBoxSlack.isChecked(), mBoxJira.isChecked(), mTitle.getText().toString());
        } else if (requestCode == RESULT_CANCELED) {
            UiUtils.showAuthError(this);
        }
    }

    @Override
    public void showLoading() {
        mMaterialDialog = UiUtils.showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mMaterialDialog != null)
            mMaterialDialog.dismiss();
    }
}
