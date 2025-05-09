/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.util.lang;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.apache.ignite.internal.util.tostring.GridToStringInclude;
import org.apache.ignite.internal.util.typedef.internal.S;
import org.jetbrains.annotations.Nullable;

/**
 * Convenience class representing mutable tuple of four values.
 * <h2 class="header">Thread Safety</h2>
 * This class doesn't provide any synchronization for multi-threaded access
 * and it is responsibility of the user of this class to provide outside
 * synchronization, if needed.
 */
public class GridTuple4<V1, V2, V3, V4> implements Iterable<Object>, Externalizable, Cloneable {
    /** */
    private static final long serialVersionUID = 0L;

    /** Value 1. */
    @GridToStringInclude
    private V1 val1;

    /** Value 2. */
    @GridToStringInclude
    private V2 val2;

    /** Value 3. */
    @GridToStringInclude
    private V3 val3;

    /** Value 4. */
    @GridToStringInclude
    private V4 val4;

    /**
     * Empty constructor required by {@link Externalizable}.
     */
    public GridTuple4() {
        // No-op.
    }

    /**
     * Fully initializes this tuple.
     *
     * @param val1 First value.
     * @param val2 Second value.
     * @param val3 Third value.
     * @param val4 Forth value.
     */
    public GridTuple4(@Nullable V1 val1, @Nullable V2 val2, @Nullable V3 val3, @Nullable V4 val4) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
    }

    /**
     * Gets first value.
     *
     * @return First value.
     */
    @Nullable public V1 get1() {
        return val1;
    }

    /**
     * Gets second value.
     *
     * @return Second value.
     */
    @Nullable public V2 get2() {
        return val2;
    }

    /**
     * Gets third value.
     *
     * @return Third value.
     */
    @Nullable public V3 get3() {
        return val3;
    }

    /**
     * Gets forth value.
     *
     * @return Forth value.
     */
    @Nullable public V4 get4() {
        return val4;
    }

    /**
     * Sets first value.
     *
     * @param val1 First value.
     */
    public void set1(@Nullable V1 val1) {
        this.val1 = val1;
    }

    /**
     * Sets second value.
     *
     * @param val2 Second value.
     */
    public void set2(@Nullable V2 val2) {
        this.val2 = val2;
    }

    /**
     * Sets third value.
     *
     * @param val3 Third value.
     */
    public void set3(@Nullable V3 val3) {
        this.val3 = val3;
    }

    /**
     * Sets fourth value.
     *
     * @param val4 Fourth value.
     */
    public void set4(@Nullable V4 val4) {
        this.val4 = val4;
    }

    /**
     * Sets all values.
     *
     * @param val1 First value.
     * @param val2 Second value.
     * @param val3 Third value.
     * @param val4 Fourth value.
     */
    public void set(@Nullable V1 val1, @Nullable V2 val2, @Nullable V3 val3, @Nullable V4 val4) {
        set1(val1);
        set2(val2);
        set3(val3);
        set4(val4);
    }

    /** {@inheritDoc} */
    @Override public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int nextIdx = 1;

            @Override public boolean hasNext() {
                return nextIdx < 5;
            }

            @Nullable @Override public Object next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                Object res = null;

                if (nextIdx == 1)
                    res = get1();
                else if (nextIdx == 2)
                    res = get2();
                else if (nextIdx == 3)
                    res = get3();
                else if (nextIdx == 4)
                    res = get4();

                nextIdx++;

                return res;
            }

            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /** {@inheritDoc} */
    @Override public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ignore) {
            throw new InternalError();
        }
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(val1);
        out.writeObject(val2);
        out.writeObject(val3);
        out.writeObject(val4);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        val1 = (V1)in.readObject();
        val2 = (V2)in.readObject();
        val3 = (V3)in.readObject();
        val4 = (V4)in.readObject();
    }

    /** {@inheritDoc} */
    @Override public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof GridTuple4))
            return false;

        GridTuple4<?, ?, ?, ?> t = (GridTuple4<?, ?, ?, ?>)o;

        return Objects.equals(val1, t.val1) && Objects.equals(val2, t.val2) && Objects.equals(val3, t.val3) && Objects.equals(val4, t.val4);
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        int res = val1 != null ? val1.hashCode() : 0;

        res = 13 * res + (val2 != null ? val2.hashCode() : 0);
        res = 17 * res + (val3 != null ? val3.hashCode() : 0);
        res = 19 * res + (val4 != null ? val4.hashCode() : 0);

        return res;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridTuple4.class, this);
    }
}
