package airsign.loopAPI;

import org.json.JSONObject;

public class GenericParser
{
    public static class ErrorResponse
    {
        private boolean err;
        private String msg;

        public ErrorResponse(boolean error, String message)
        {
            err = error;
            msg = message;
        }
        public boolean isError() { return err; }
        public String getErrorMessage() { return msg; }
    }

    public static ErrorResponse getError(JSONObject jo)
    {
        final String
            kError = "error",
            kMessage = "message";
        try
        {
            JSONObject json = jo.optJSONObject(kError);
            if (json==null) return new ErrorResponse(false, null);
            else
            {
                String msg = json.optString(kMessage);
                return new ErrorResponse(true, msg);
            }
        }
        catch (Exception e) { return new ErrorResponse(true, null); }
    }

}
