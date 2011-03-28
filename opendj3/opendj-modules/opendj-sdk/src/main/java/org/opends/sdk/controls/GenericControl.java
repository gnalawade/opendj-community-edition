/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2010 Sun Microsystems, Inc.
 */
package org.opends.sdk.controls;



import org.opends.sdk.ByteString;

import com.sun.opends.sdk.util.StaticUtils;
import com.sun.opends.sdk.util.Validator;



/**
 * A generic control which can be used to represent arbitrary raw request and
 * response controls.
 */
public final class GenericControl implements Control
{

  /**
   * Creates a new control having the same OID, criticality, and value as the
   * provided control.
   *
   * @param control
   *          The control to be copied.
   * @return The new control.
   * @throws NullPointerException
   *           If {@code control} was {@code null}.
   */
  public static GenericControl newControl(final Control control)
      throws NullPointerException
  {
    Validator.ensureNotNull(control);

    if (control instanceof GenericControl)
    {
      return (GenericControl) control;
    }

    return new GenericControl(control.getOID(), control.isCritical(), control
        .getValue());
  }



  /**
   * Creates a new non-critical control having the provided OID and no value.
   *
   * @param oid
   *          The numeric OID associated with this control.
   * @return The new control.
   * @throws NullPointerException
   *           If {@code oid} was {@code null}.
   */
  public static GenericControl newControl(final String oid)
      throws NullPointerException
  {
    return new GenericControl(oid, false, null);
  }



  /**
   * Creates a new control having the provided OID and criticality, but no
   * value.
   *
   * @param oid
   *          The numeric OID associated with this control.
   * @param isCritical
   *          {@code true} if it is unacceptable to perform the operation
   *          without applying the semantics of this control, or {@code false}
   *          if it can be ignored.
   * @return The new control.
   * @throws NullPointerException
   *           If {@code oid} was {@code null}.
   */
  public static GenericControl newControl(final String oid,
      final boolean isCritical) throws NullPointerException
  {
    return new GenericControl(oid, isCritical, null);
  }



  /**
   * Creates a new control having the provided OID, criticality, and value.
   * <p>
   * If {@code value} is not an instance of {@code ByteString} then it will be
   * converted using the {@link ByteString#valueOf(Object)} method.
   *
   * @param oid
   *          The numeric OID associated with this control.
   * @param isCritical
   *          {@code true} if it is unacceptable to perform the operation
   *          without applying the semantics of this control, or {@code false}
   *          if it can be ignored.
   * @param value
   *          The value associated with this control, or {@code null} if there
   *          is no value. Its format is defined by the specification of this
   *          control.
   * @return The new control.
   * @throws NullPointerException
   *           If {@code oid} was {@code null}.
   */
  public static GenericControl newControl(final String oid,
      final boolean isCritical, final Object value) throws NullPointerException
  {
    return new GenericControl(oid, isCritical, (value == null) ? null
        : ByteString.valueOf(value));
  }



  private final String oid;

  private final boolean isCritical;

  private final ByteString value;



  // Prevent direct instantiation.
  private GenericControl(final String oid, final boolean isCritical,
      final ByteString value)
  {
    Validator.ensureNotNull(oid);
    this.oid = oid;
    this.isCritical = isCritical;
    this.value = value;
  }



  /**
   * {@inheritDoc}
   */
  public String getOID()
  {
    return oid;
  }



  /**
   * {@inheritDoc}
   */
  public ByteString getValue()
  {
    return value;
  }



  /**
   * {@inheritDoc}
   */
  public boolean hasValue()
  {
    return value != null;
  }



  /**
   * {@inheritDoc}
   */
  public boolean isCritical()
  {
    return isCritical;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("Control(oid=");
    builder.append(getOID());
    builder.append(", criticality=");
    builder.append(isCritical());
    if (value != null)
    {
      builder.append(", value=");
      StaticUtils.toHexPlusAscii(value, builder, 4);
    }
    builder.append(")");
    return builder.toString();
  }

}