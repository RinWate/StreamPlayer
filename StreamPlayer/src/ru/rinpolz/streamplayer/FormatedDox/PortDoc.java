package ru.rinpolz.streamplayer.FormatedDox;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import ru.rinpolz.streamplayer.utill.Utils;

public class PortDoc extends PlainDocument {
	private static final long serialVersionUID = 1L;

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

		if (str.length() <= 5 && this.getLength() <= 5 - str.length()) {
			char[] ch = str.toCharArray();
			for (char c : ch) {
				if (c > 57 || c < 48) {
					Utils.BeePBoop();
					return;
				}
			}
			super.insertString(offs, str, a);
		} else {
			Utils.BeePBoop();
		}

	}

}
