/******************************************************************************
 * Copyright (c) 2018 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/

package org.eclipse.payara.tools.sdk.utils;

import org.eclipse.payara.tools.sdk.logging.Logger;

/**
 * Cyclic <code>String</code> buffer.
 * <p/>
 * Stores up to <code>&lt;size&gt;</code> characters in cyclic buffer and allows to simply append
 * new characters to the end or prepend new characters to the beginning of the buffer without
 * necessity to move buffer content.
 * <p/>
 *
 * @author Tomas Kraus
 */
public class CyclicStringBuffer {

    ////////////////////////////////////////////////////////////////////////////
    // Class attributes //
    ////////////////////////////////////////////////////////////////////////////

    /** Logger instance for this class. */
    private static final Logger LOGGER = new Logger(CyclicStringBuffer.class);

    ////////////////////////////////////////////////////////////////////////////
    // Instance attributes //
    ////////////////////////////////////////////////////////////////////////////

    /** Buffer size. */
    private int size;

    /** Buffer content. */
    private char[] buff;

    /** Count of valid characters in the buffer. */
    private int len;

    /** Current beginning of valid characters storage. */
    private int beg;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors //
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates an instance of cyclic <code>String</code> buffer.
     * <p/>
     *
     * @param size Cyclic <code>String</code> buffer size.
     */
    public CyclicStringBuffer(final int size) {
        this.size = size;
        this.buff = new char[size];
        len = 0;
        beg = 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods //
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Resize cyclic <code>String</code> buffer.
     * <p/>
     * Content of buffer will be removed.
     * <p/>
     *
     * @param size New cyclic <code>String</code> buffer size.
     */
    public void resize(final int size) {
        if (this.size != size) {
            this.size = size;
            this.buff = new char[size];
        }
        len = 0;
        beg = 0;
    }

    /**
     * Appends character at the end of the buffer.
     * <p/>
     * First character in the buffer will be discarded when buffer is full.
     * <p/>
     *
     * @param c Character to be appended.
     * @return Value of <code>true</code> when buffer was full and first character in the buffer got
     * overwritten or <code>false</code> otherwise.
     */
    public boolean append(final char c) {
        buff[(beg + len) % size] = c;
        if (len == size) {
            beg = (beg + 1) % size;
            return true;
        } else {
            len += 1;
            return false;
        }
    }

    /**
     * Appends character at the beginning of the buffer.
     * <p/>
     * Last character in the buffer will be discarded when buffer is full.
     * <p/>
     *
     * @param c Character to be prepended.
     * @return Value of <code>true</code> when buffer was full and last character in the buffer got
     * overwritten or <code>false</code> otherwise.
     */
    public boolean prepend(final char c) {
        beg = (beg + size - 1) % size;
        buff[beg] = c;
        if (len == size) {
            return true;
        } else {
            len += 1;
            return false;
        }
    }

    /**
     * Compares buffer content to the specific {@link String}.
     * <p/>
     * Empty {@link String} value and <code>null</code> value are considered as equivalent and are equal
     * to zero length buffer content.
     * <p/>
     *
     * @return Value of <code>true</code> if buffer content represents {@link String} equivalent to this
     * provided string or <code>false</code> otherwise.
     */
    public boolean equals(final String s) {
        // Handle null value.
        if (s == null) {
            return len == 0;
        }
        // Cut evaluation for differend lengths.
        if (len != s.length()) {
            return false;
        }
        boolean result = true;
        for (int i = 0; i < len; i++) {
            if (buff[(beg + i) % size] != s.charAt(i)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Returns {@link String} stored in buffer.
     * <p/>
     * Zero length content is returned as zero length {@link String};
     * <p/>
     *
     * @return {@link String} stored in buffer.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(buff[(beg + i) % size]);
        }
        return sb.toString();
    }

}
