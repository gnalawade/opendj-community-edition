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

package org.forgerock.opendj.ldap.requests;



/**
 * An abstract Bind request which can be used as the basis for implementing new
 * authentication methods.
 *
 * @param <R>
 *          The type of Bind request.
 */
abstract class AbstractBindRequest<R extends BindRequest> extends
    AbstractRequestImpl<R> implements BindRequest
{

  /**
   * Creates a new abstract bind request.
   */
  protected AbstractBindRequest()
  {
    // Nothing to do.
  }



  /**
   * Creates a new abstract bind request that is an exact copy of the provided
   * request.
   *
   * @param bindRequest
   *          The bind request to be copied.
   * @throws NullPointerException
   *           If {@code bindRequest} was {@code null} .
   */
  protected AbstractBindRequest(BindRequest bindRequest)
  {
    super(bindRequest);
  }



  /**
   * {@inheritDoc}
   */
  public abstract String getName();



  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  final R getThis()
  {
    return (R) this;
  }

}