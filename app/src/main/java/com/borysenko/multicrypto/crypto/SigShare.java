package com.borysenko.multicrypto.crypto;

import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;

import static com.borysenko.multicrypto.tools.Extensions.factorial;
import static com.borysenko.multicrypto.tools.Extensions.lambda;

public class SigShare {

    private final static boolean CHECK_VERIFIER = true;

    private int id;

    private BigInteger sig;

    private Verifier sigVerifier;

    SigShare(final int id, final BigInteger sig, final Verifier sigVerifier) {
        this.id = id;
        this.sig = sig;
        this.sigVerifier = sigVerifier;
    }

    public SigShare(final int id, final byte[] sig) {
        this.id = id;
        this.sig = new BigInteger(sig);
    }

    public int getId() {
        return this.id;
    }

    private BigInteger getSig() {
        return this.sig;
    }

    private Verifier getSigVerifier() {
        return this.sigVerifier;
    }

    public byte[] getBytes() {
        return this.sig.toByteArray();
    }

    public static boolean verify(final byte[] data, final SigShare[] sigs,
                                 final int k, final int N, final BigInteger n, final BigInteger e)
            throws ThresholdSigException {

        final boolean[] haveSig = new boolean[N];
        for (int i = 0; i < k; i++) {
            if (sigs[i] == null)
                throw new ThresholdSigException("Null signature");
            if (haveSig[sigs[i].getId() - 1])
                throw new ThresholdSigException("Duplicate signature: "
                        + sigs[i].getId());
            haveSig[sigs[i].getId() - 1] = true;
        }

        final BigInteger x = (new BigInteger(data)).mod(n);
        final BigInteger delta = factorial(N);

        if (CHECK_VERIFIER) {
            final BigInteger FOUR = BigInteger.valueOf(4L);
            final BigInteger TWO = BigInteger.valueOf(2L);
            final BigInteger xTilde = x.modPow(FOUR.multiply(delta), n);

            try {
                final MessageDigest md = MessageDigest.getInstance("SHA");

                for (int i = 0; i < k; i++) {
                    md.reset();
                    final Verifier ver = sigs[i].getSigVerifier();
                    final BigInteger v = ver.getGroupVerifier();
                    final BigInteger vi = ver.getShareVerifier();

                    md.update(v.toByteArray());

                    md.update(xTilde.toByteArray());

                    md.update(vi.toByteArray());

                    final BigInteger xi = sigs[i].getSig();
                    md.update(xi.modPow(TWO, n).toByteArray());

                    final BigInteger vz = v.modPow(ver.getZ(), n);

                    final BigInteger vinegc = vi.modPow(ver.getC(), n).modInverse(n);
                    md.update(vz.multiply(vinegc).mod(n).toByteArray());

                    final BigInteger xtildez = xTilde.modPow(ver.getZ(), n);

                    final BigInteger xineg2c = xi.modPow(ver.getC(), n).modInverse(n);

                    md.update(xineg2c.multiply(xtildez).mod(n).toByteArray());
                    final BigInteger result = new BigInteger(md.digest()).mod(n);

                    if (!result.equals(ver.getC())) {
                        Log.e("share verifier", "Share verifier is not OK");
                        return false;
                    }
                }
            } catch (final java.security.NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        }

        BigInteger w = BigInteger.valueOf(1L);

        for (int i = 0; i < k; i++)
            w = w.multiply(sigs[i].getSig().modPow(
                    lambda(sigs[i].getId(), sigs, delta), n));

        // eprime = delta^2*4
        final BigInteger eprime = delta.multiply(delta).shiftLeft(2);

        w = w.mod(n);
        final BigInteger xeprime = x.modPow(eprime, n);
        final BigInteger we = w.modPow(e, n);
        return (xeprime.compareTo(we) == 0);
    }
}
