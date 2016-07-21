package com.md.hjyg.layoutEntities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 键盘界面(字母，数字)
 */
public class KeyBoardView extends PopupWindow implements OnClickListener {
	private LayoutInflater mInflater;
	private View mView;
	private List<EditText> mEditTextList = new ArrayList<EditText>();

	private Button btnX;
	private Context context;
	private StringBuffer input;
	// 三大布局
	private LinearLayout viewKeyboard, llNumKeyBoard;
	// 数字键盘键
	private Button btnNum0, btnNum1, btnNum2, btnNum3, btnNum4, btnNum5,
			btnNum6, btnNum7, btnNum8, btnNum9;
	// 功能鍵
	private LinearLayout btnDel1;
	//关闭
	private LinearLayout close_ll;

	private int length;
	// 是否为大写
	private boolean isLower = true;

	// 是否往上顶
	private boolean isTopMargin = false;
	private ClosePopupWindowListener closePopupWindowListener;

	public boolean isTopMargin() {
		return isTopMargin;
	}

	public void setTopMargin(boolean isTopMargin) {
		this.isTopMargin = isTopMargin;
	}

	// 主布局，popupwindow下面的布局，需要传值过来才能改变它的参数。
	private View llMainView;

	public KeyBoardView(Context context, List<EditText> mEditTextList,
			View llMainView) {
		super(context);
		this.context = context;
		this.mEditTextList = mEditTextList;
		this.llMainView = llMainView;
		mInflater = LayoutInflater.from(context);
		mView = mInflater.inflate(R.layout.view_keyboard, null);
		setContentView(mView);
		// setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		initView();
		InitData();
		InitAction();
	}

	private void initView() {
		llNumKeyBoard = (LinearLayout) mView.findViewById(R.id.llNumKeyBoard);
		viewKeyboard = (LinearLayout) mView.findViewById(R.id.view_keyboard);

		// 数字
		btnNum0 = (Button) mView.findViewById(R.id.btnNum0);
		btnNum1 = (Button) mView.findViewById(R.id.btnNum1);
		btnNum2 = (Button) mView.findViewById(R.id.btnNum2);
		btnNum3 = (Button) mView.findViewById(R.id.btnNum3);
		btnNum4 = (Button) mView.findViewById(R.id.btnNum4);
		btnNum5 = (Button) mView.findViewById(R.id.btnNum5);
		btnNum6 = (Button) mView.findViewById(R.id.btnNum6);
		btnNum7 = (Button) mView.findViewById(R.id.btnNum7);
		btnNum8 = (Button) mView.findViewById(R.id.btnNum8);
		btnNum9 = (Button) mView.findViewById(R.id.btnNum9);

		btnX = (Button) mView.findViewById(R.id.keypad_sign_x);
		btnDel1 = (LinearLayout) mView.findViewById(R.id.keypad_sign_del1);
		close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
	}

	private void InitData() {
		input = new StringBuffer();
		input.append(getCurrentFocus().getText());
	}

	private void InitAction() {
		btnNum0.setOnClickListener(this);
		btnNum1.setOnClickListener(this);
		btnNum2.setOnClickListener(this);
		btnNum3.setOnClickListener(this);
		btnNum4.setOnClickListener(this);
		btnNum5.setOnClickListener(this);
		btnNum6.setOnClickListener(this);
		btnNum7.setOnClickListener(this);
		btnNum8.setOnClickListener(this);
		btnNum9.setOnClickListener(this);
		btnX.setOnClickListener(this);
		// btnNumb.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // llWordKeyBoard.setVisibility(View.GONE);
		// llNumKeyBoard.setVisibility(View.VISIBLE);
		// btnNumb1.setBackgroundResource(R.drawable. h_click);
		// btnWord1.setBackgroundResource(R.drawable. h_default);
		// }
		// });
		// btnWord.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// btnWord.setBackgroundResource(R.drawable. h);
		// }
		// });
		// btnBar.setOnClickListener(this);
		// btnDel.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// toDelete();
		// }
		// });
		// btnDel.setOnLongClickListener(mListener);
		// btnChg.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// changeLower();
		// }
		// });
		// btnClose.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// close();
		// }
		// });
		// btnUp.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// moveUp();
		// }
		// });
		// btnDown.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// moveDown();
		// }
		// });
		// btnBack.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int index = getCurrentFocus().getSelectionStart();
		// moveRight(++index);
		// }
		// });
		//
		// btnFront.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int index = getCurrentFocus().getSelectionStart();
		// moveLeft(--index);
		// }
		// });

		// btnNumb1.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// btnNumb.setBackgroundResource(R.drawable. h_default);
		// btnWord1.setBackgroundResource(R.drawable.h_click);
		//
		// }
		// });
		// btnWord1.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// llNumKeyBoard.setVisibility(View.GONE);
		// // llWordKeyBoard.setVisibility(View.VISIBLE);
		// btnNumb.setBackgroundResource(R.drawable. h_default);
		// btnWord.setBackgroundResource(R.drawable. h_click);
		//
		// }
		// });
		// btnBar1.setOnClickListener(this);
		btnDel1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toDelete();
			}
		});
		btnDel1.setOnLongClickListener(mListener);
		// btnChg1.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// changeLower();
		// }
		// });
		close_ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});
		// btnDown1.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// moveDown();
		// }
		// });
		// btnUp1.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// moveUp();
		// }
		// });
		// btnBack1.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int index = getCurrentFocus().getSelectionStart();
		// moveRight(++index);
		// }
		// });
		//
		// btnFront1.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int index = getCurrentFocus().getSelectionStart();
		// moveLeft(--index);
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		int index = getCurrentFocus().getSelectionStart();
		numWordClick(v, ++index);
		// otherClick(v,index);
	}

	public void numWordClick(View v, int index) {
		switch (v.getId()) {
		case R.id.keypad_sign_x:
		case R.id.btnNum1:
		case R.id.btnNum2:
		case R.id.btnNum3:
		case R.id.btnNum4:
		case R.id.btnNum5:
		case R.id.btnNum6:
		case R.id.btnNum7:
		case R.id.btnNum8:
		case R.id.btnNum9:
		case R.id.btnNum0:
		case R.id.keypad_sign_bar:
			// case R.id.keypad_sign_bar1:
			// case R.id.btnf1:
		case R.id.btnf2:
			toInput((Button) v);
			break;
		default:
			break;
		}
		// 确定光标的位置在最后边

		CharSequence text = getCurrentFocus().getText();
		if (index < text.length()) {
			getCurrentFocus().setSelection(index);
		} else {
			getCurrentFocus().setSelection(text.length());
		}

	}

	// 切换大小写英文字母
	private void toChangeBigorSmallWord(int m) {
		if (m == 0) {
			btnX.setText("X");
		} else {
			btnX.setText("x");
		}
	}

	// 长按del键,清空EditText
	private OnLongClickListener mListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			int length = input.length();
			if (length > 0) {
				input.delete(0, input.length());
				getCurrentFocus().setText(input);
			}
			return false;
		}
	};

	// 用于长按某一按钮时情况EditText
	private void toDelete() {
		InitData();
		int index = getCurrentFocus().getSelectionStart();
		length = input.length();
		if (length > 0 && index > 0) {
			input = input.deleteCharAt(index - 1);
			getCurrentFocus().setText(input);
			// Toast.makeText(context, "光标位置："+index, 20).show();
			getCurrentFocus().setSelection(index - 1);
		}
	}

	// 用于点击按钮输入
	private void toInput(Button btn) {
		InitData();
		int index = getCurrentFocus().getSelectionStart();
		input.insert(index, btn.getText().toString());
		getCurrentFocus().setText(input.toString());
	}

	private void changeLower() {
		if (isLower) {
			toChangeBigorSmallWord(0);
			isLower = false;
		} else {
			toChangeBigorSmallWord(1);
			isLower = true;
		}
	}

	private EditText getCurrentFocus() {
		for (EditText et : mEditTextList) {
			if (et.isFocused()) {
				return et;
			}
		}
		return null;
	}

	public void moveDown() {
		int done_input_id = getCurrentFocus().getId();
		for (int i = 0; i < mEditTextList.size(); i++) {
			if (mEditTextList.get(i).getId() == done_input_id) {
				int next_index = i + 1;
				try {
					EditText next_edit = mEditTextList.get(next_index);
					next_edit.requestFocus();
					if (isTopMargin) {
						LayoutParams lp1 = (LayoutParams) llMainView
								.getLayoutParams();
						lp1.topMargin = -150 * next_index;
						llMainView.requestLayout();
					}
				} catch (Exception e) {
				}
			}
		}
		InitData();
	}

	public void moveUp() {
		int done_input_id = getCurrentFocus().getId();
		for (int i = 0; i < mEditTextList.size(); i++) {
			if (mEditTextList.get(i).getId() == done_input_id) {
				int prov_index = i - 1;
				try {
					EditText prov_edit = mEditTextList.get(prov_index);
					prov_edit.requestFocus();
					if (isTopMargin) {
						LayoutParams lp1 = (LayoutParams) llMainView
								.getLayoutParams();
						lp1.topMargin = -150 * prov_index;
						llMainView.requestLayout();
					}
				} catch (Exception e) {
				}
			}
		}
		InitData();
	}

	private void moveLeft(int index) {
		if (index >= 0) {
			getCurrentFocus().setSelection(index);
		}
	}

	private void moveRight(int index) {
		CharSequence text = getCurrentFocus().getText();
		if (index <= text.length()) {
			getCurrentFocus().setSelection(index);
		}
	}

	public void close() {
		viewKeyboard.setVisibility(View.GONE);
		LayoutParams lp1 = (LayoutParams) llMainView.getLayoutParams();
		lp1.topMargin = 0;
		llMainView.requestLayout();
		if (closePopupWindowListener != null) {
			closePopupWindowListener.intitLayout();
		}
	}

	public void open() {
		viewKeyboard.setVisibility(View.VISIBLE);
	}
	
	public void setCloseListener(ClosePopupWindowListener l){
		this.closePopupWindowListener = l;
	}
	
	public interface ClosePopupWindowListener{
		public void intitLayout();
	}
}
