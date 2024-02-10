import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class InputTextFilter extends DocumentFilter {

    @Override
    public void replace(FilterBypass fb, int i, int i1, String string, AttributeSet as) throws BadLocationException {
        // stringa může mít více znaku když se do inputu vkládá přes ctrl+v
        for (int n = string.length(); n > 0; n--) {
            char currChar = string.charAt(n - 1);
            boolean isPreviousWhiteSpace = false;
            if (n > 1) {
                char prevChar = string.charAt(n - 2);
                isPreviousWhiteSpace = Character.isWhitespace(prevChar);
            }
            if (Character.isWhitespace(currChar) && isPreviousWhiteSpace) {
                return;
            }
            super.replace(fb, i, i1, String.valueOf(currChar), as);
        }
    }

    @Override
    public void remove(FilterBypass fb, int i, int i1) throws BadLocationException {
        super.remove(fb, i, i1);
    }

    @Override
    public void insertString(FilterBypass fb, int i, String string, AttributeSet as) throws BadLocationException {
        super.insertString(fb, i, string, as);
    }

}

