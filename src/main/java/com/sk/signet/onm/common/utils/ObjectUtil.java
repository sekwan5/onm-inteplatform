/* ---------------------------------------------------------------------
 * @(#)ObjectUtil.java 
 * @Creator    yhlee (yhlee@handysoft.co.kr)
 * @version    1.0
 * @date       2006-04-05
 * ---------------------------------------------------------------------
 */
package com.sk.signet.onm.common.utils;

import java.io.Serializable;

/**
 * <p>
 * Operations on <code>Object</code>.
 * </p>
 * 
 * <p>
 * This class tries to handle <code>null</code> input gracefully.
 * An exception will generally not be thrown for a <code>null</code> input.
 * Each method documents its behaviour in more detail.
 * </p>
 *
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @author <a href="mailto:janekdb@yahoo.co.uk">Janek Bogucki</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author Stephen Colebourne
 * @author Gary Gregory
 * @since 1.0
 * @version $Id: ObjectUtil.java,v 1.1 2009/04/07 10:45:23 since88 Exp $
 */
public class ObjectUtil {

    /**
     * <p>
     * Singleton used as a <code>null</code> placeholder where
     * <code>null</code> has another meaning.
     * </p>
     *
     * <p>
     * For example, in a <code>HashMap</code> the
     * {@link java.util.HashMap#get(java.lang.Object)} method returns
     * <code>null</code> if the <code>Map</code> contains
     * <code>null</code> or if there is no matching key. The
     * <code>Null</code> placeholder can be used to distinguish between
     * these two cases.
     * </p>
     *
     * <p>
     * Another example is <code>Hashtable</code>, where <code>null</code>
     * cannot be stored.
     * </p>
     *
     * <p>
     * This instance is Serializable.
     * </p>
     */
    public static final Null NULL = new Null();

    /**
     * <p>
     * <code>ObjectUtil</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>ObjectUtil.defaultIfNull("a","b");</code>.
     * </p>
     *
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance
     * to operate.
     * </p>
     */
    public ObjectUtil() {
    }

    // Defaulting
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Returns a default value if the object passed is
     * <code>null</code>.
     * </p>
     * 
     * <pre>
     * ObjectUtil.defaultIfNull(null, null)      = null
     * ObjectUtil.defaultIfNull(null, "")        = ""
     * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
     * ObjectUtil.defaultIfNull("abc", *)        = "abc"
     * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param object       the <code>Object</code> to test, may be <code>null</code>
     * @param defaultValue the default value to return, may be <code>null</code>
     * @return <code>object</code> if it is not <code>null</code>, defaultValue
     *         otherwise
     */
    public static Object defaultIfNull(Object object, Object defaultValue) {
        return (object != null ? object : defaultValue);
    }

    /**
     * <p>
     * Compares two objects for equality, where either one or both
     * objects may be <code>null</code>.
     * </p>
     *
     * <pre>
     * ObjectUtil.equals(null, null)                  = true
     * ObjectUtil.equals(null, "")                    = false
     * ObjectUtil.equals("", null)                    = false
     * ObjectUtil.equals("", "")                      = true
     * ObjectUtil.equals(Boolean.TRUE, null)          = false
     * ObjectUtil.equals(Boolean.TRUE, "true")        = false
     * ObjectUtil.equals(Boolean.TRUE, Boolean.TRUE)  = true
     * ObjectUtil.equals(Boolean.TRUE, Boolean.FALSE) = false
     * </pre>
     *
     * @param object1 the first object, may be <code>null</code>
     * @param object2 the second object, may be <code>null</code>
     * @return <code>true</code> if the values of both objects are the same
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    // Identity ToString
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will return <code>null</code>.
     * </p>
     *
     * <pre>
     * ObjectUtil.identityToString(null)         = null
     * ObjectUtil.identityToString("")           = "java.lang.String@1e23"
     * ObjectUtil.identityToString(Boolean.TRUE) = "java.lang.Boolean@7fa"
     * </pre>
     *
     * @param object the object to create a toString for, may be
     *               <code>null</code>
     * @return the default toString text, or <code>null</code> if
     *         <code>null</code> passed in
     */
    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        return appendIdentityToString(null, object).toString();
    }

    /**
     * <p>
     * Appends the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will return <code>null</code>.
     * </p>
     *
     * <pre>
     * ObjectUtil.appendIdentityToString(*, null)            = null
     * ObjectUtil.appendIdentityToString(null, "")           = "java.lang.String@1e23"
     * ObjectUtil.appendIdentityToString(null, Boolean.TRUE) = "java.lang.Boolean@7fa"
     * ObjectUtil.appendIdentityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
     * </pre>
     *
     * @param buffer the buffer to append to, may be <code>null</code>
     * @param object the object to create a toString for, may be <code>null</code>
     * @return the default toString text, or <code>null</code> if
     *         <code>null</code> passed in
     * @since 2.0
     */
    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }
        if (buffer == null) {
            buffer = new StringBuffer();
        }
        return buffer
                .append(object.getClass().getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(object)));
    }

    // ToString
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the <code>toString</code> of an <code>Object</code> returning
     * an empty string ("") if <code>null</code> input.
     * </p>
     * 
     * <pre>
     * ObjectUtil.toString(null)         = ""
     * ObjectUtil.toString("")           = ""
     * ObjectUtil.toString("bat")        = "bat"
     * ObjectUtil.toString(Boolean.TRUE) = "true"
     * </pre>
     * 
     * @see StringUtils#defaultString(String)
     * @see String#valueOf(Object)
     * @param obj the Object to <code>toString</code>, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code>
     *         input
     * @since 2.0
     */
    public static String toString(Object obj) {
        return (obj == null ? "" : obj.toString());
    }

    /**
     * <p>
     * Gets the <code>toString</code> of an <code>Object</code> returning
     * a specified text if <code>null</code> input.
     * </p>
     * 
     * <pre>
     * ObjectUtil.toString(null, null)           = null
     * ObjectUtil.toString(null, "null")         = "null"
     * ObjectUtil.toString("", "null")           = ""
     * ObjectUtil.toString("bat", "null")        = "bat"
     * ObjectUtil.toString(Boolean.TRUE, "null") = "true"
     * </pre>
     * 
     * @see StringUtils#defaultString(String,String)
     * @see String#valueOf(Object)
     * @param obj     the Object to <code>toString</code>, may be null
     * @param nullStr the String to return if <code>null</code> input, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code>
     *         input
     * @since 2.0
     */
    public static String toString(Object obj, String nullStr) {
        return (obj == null ? nullStr : obj.toString());
    }

    // Null
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Class used as a null placeholder where <code>null</code>
     * has another meaning.
     * </p>
     *
     * <p>
     * For example, in a <code>HashMap</code> the
     * {@link java.util.HashMap#get(java.lang.Object)} method returns
     * <code>null</code> if the <code>Map</code> contains
     * <code>null</code> or if there is no matching key. The
     * <code>Null</code> placeholder can be used to distinguish between
     * these two cases.
     * </p>
     *
     * <p>
     * Another example is <code>Hashtable</code>, where <code>null</code>
     * cannot be stored.
     * </p>
     */
    public static class Null implements Serializable {
        // declare serialization compatability with Commons Lang 1.0
        private static final long serialVersionUID = 7092611880189329093L;

        /**
         * Restricted constructor - singleton.
         */
        Null() {
        }

        /**
         * <p>
         * Ensure singleton.
         * </p>
         * 
         * @return the singleton value
         */
        private Object readResolve() {
            return ObjectUtil.NULL;
        }
    }

}
