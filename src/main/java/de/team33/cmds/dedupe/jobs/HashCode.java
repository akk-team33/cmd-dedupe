package de.team33.cmds.dedupe.jobs;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class HashCode {

    {
        final HashFunction sha256 = Hashing.sha256();
        final Hasher hasher = sha256.newHasher();
        //hasher.
    }
}
