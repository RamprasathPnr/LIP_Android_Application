package com.google.zxing.client.android.result;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.example.user.lip.R;

import android.app.Activity;
public final class EmailAddressResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_email,
      R.string.button_add_contact
  };

  public EmailAddressResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
  }

  @Override
  public int getButtonCount() {
    return buttons.length;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    EmailAddressParsedResult emailResult = (EmailAddressParsedResult) getResult();
    switch (index) {
      case 0:
        sendEmail(emailResult.getTos(),
                  emailResult.getCCs(),
                  emailResult.getBCCs(),
                  emailResult.getSubject(),
                  emailResult.getBody());
        break;
      case 1:
        addEmailOnlyContact(emailResult.getTos(), null);
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_email_address;
  }
}
