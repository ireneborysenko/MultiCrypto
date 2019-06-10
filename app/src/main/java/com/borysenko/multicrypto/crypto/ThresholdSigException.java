package com.borysenko.multicrypto.crypto;

class ThresholdSigException extends RuntimeException {
    private static final long serialVersionUID = 2266413730951237508L;

    private static String diagnostic = "Threshold Signature Exception";

    ThresholdSigException(final String detail) {
        super(diagnostic + ": " + detail);
    }
}
