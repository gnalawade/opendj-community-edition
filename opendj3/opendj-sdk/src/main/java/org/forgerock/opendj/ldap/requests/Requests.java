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



import static org.forgerock.opendj.ldap.CoreMessages.WARN_READ_LDIF_RECORD_CHANGE_RECORD_WRONG_TYPE;

import javax.net.ssl.SSLContext;
import javax.security.auth.Subject;

import org.forgerock.i18n.LocalizableMessage;
import org.forgerock.i18n.LocalizedIllegalArgumentException;
import org.forgerock.opendj.ldap.*;
import org.forgerock.opendj.ldif.ChangeRecord;
import org.forgerock.opendj.ldif.LDIFChangeRecordReader;

import com.forgerock.opendj.util.Validator;



/**
 * This class contains various methods for creating and manipulating requests.
 * <p>
 * All copy constructors of the form {@code copyOfXXXRequest} perform deep
 * copies of their request parameter. More specifically, any controls,
 * modifications, and attributes contained within the response will be
 * duplicated.
 * <p>
 * Similarly, all unmodifiable views of request returned by methods of the form
 * {@code unmodifiableXXXRequest} return deep unmodifiable views of their
 * request parameter. More specifically, any controls, modifications, and
 * attributes contained within the returned request will be unmodifiable.
 */
public final class Requests
{

  // TODO: search request from LDAP URL.

  // TODO: update request from persistent search result.

  // TODO: synchronized requests?

  /**
   * Creates a new abandon request using the provided message ID.
   *
   * @param requestID
   *          The request ID of the request to be abandoned.
   * @return The new abandon request.
   */
  public static AbandonRequest newAbandonRequest(final int requestID)
  {
    return new AbandonRequestImpl(requestID);
  }



  /**
   * Creates a new add request using the provided distinguished name.
   *
   * @param name
   *          The distinguished name of the entry to be added.
   * @return The new add request.
   * @throws NullPointerException
   *           If {@code name} was {@code null}.
   */
  public static AddRequest newAddRequest(final DN name)
      throws NullPointerException
  {
    final Entry entry = new LinkedHashMapEntry().setName(name);
    return new AddRequestImpl(entry);
  }



  /**
   * Creates a new add request backed by the provided entry. Modifications made
   * to {@code entry} will be reflected in the returned add request. The
   * returned add request supports updates to its list of controls, as well as
   * updates to the name and attributes if the underlying entry allows.
   *
   * @param entry
   *          The entry to be added.
   * @return The new add request.
   * @throws NullPointerException
   *           If {@code entry} was {@code null} .
   */
  public static AddRequest newAddRequest(final Entry entry)
      throws NullPointerException
  {
    Validator.ensureNotNull(entry);
    return new AddRequestImpl(entry);
  }



  /**
   * Creates a new add request using the provided distinguished name decoded
   * using the default schema.
   *
   * @param name
   *          The distinguished name of the entry to be added.
   * @return The new add request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code name} could not be decoded using the default schema.
   * @throws NullPointerException
   *           If {@code name} was {@code null}.
   */
  public static AddRequest newAddRequest(final String name)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    final Entry entry = new LinkedHashMapEntry().setName(name);
    return new AddRequestImpl(entry);
  }



  /**
   * Creates a new add request using the provided lines of LDIF decoded using
   * the default schema.
   *
   * @param ldifLines
   *          Lines of LDIF containing an LDIF add change record or an LDIF
   *          entry record.
   * @return The new add request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code ldifLines} was empty, or contained invalid LDIF, or
   *           could not be decoded using the default schema.
   * @throws NullPointerException
   *           If {@code ldifLines} was {@code null} .
   */
  public static AddRequest newAddRequest(final String... ldifLines)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    // LDIF change record reader is tolerant to missing change types.
    final ChangeRecord record = LDIFChangeRecordReader
        .valueOfLDIFChangeRecord(ldifLines);

    if (record instanceof AddRequest)
    {
      return (AddRequest) record;
    }
    else
    {
      // Wrong change type.
      final LocalizableMessage message = WARN_READ_LDIF_RECORD_CHANGE_RECORD_WRONG_TYPE
          .get("add");
      throw new LocalizedIllegalArgumentException(message);
    }
  }



  /**
   * Creates a new anonymous SASL bind request having the provided trace string.
   *
   * @param traceString
   *          The trace information, which has no semantic value, and can be
   *          used by administrators in order to identify the user.
   * @return The new anonymous SASL bind request.
   * @throws NullPointerException
   *           If {@code traceString} was {@code null}.
   */
  public static AnonymousSASLBindRequest newAnonymousSASLBindRequest(
      final String traceString) throws NullPointerException
  {
    return new AnonymousSASLBindRequestImpl(traceString);
  }



  /**
   * Creates a new cancel extended request using the provided message ID.
   *
   * @param requestID
   *          The request ID of the request to be abandoned.
   * @return The new cancel extended request.
   */
  public static CancelExtendedRequest newCancelExtendedRequest(
      final int requestID)
  {
    return new CancelExtendedRequestImpl(requestID);
  }



  /**
   * Creates a new change record (an add, delete, modify, or modify DN request)
   * using the provided lines of LDIF decoded using the default schema.
   *
   * @param ldifLines
   *          Lines of LDIF containing an LDIF change record or an LDIF entry
   *          record.
   * @return The new change record.
   * @throws LocalizedIllegalArgumentException
   *           If {@code ldifLines} was empty, or contained invalid LDIF, or
   *           could not be decoded using the default schema.
   * @throws NullPointerException
   *           If {@code ldifLines} was {@code null} .
   */
  public static ChangeRecord newChangeRecord(final String... ldifLines)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    // LDIF change record reader is tolerant to missing change types.
    return LDIFChangeRecordReader.valueOfLDIFChangeRecord(ldifLines);
  }



  /**
   * Creates a new compare request using the provided distinguished name,
   * attribute name, and assertion value.
   *
   * @param name
   *          The distinguished name of the entry to be compared.
   * @param attributeDescription
   *          The name of the attribute to be compared.
   * @param assertionValue
   *          The assertion value to be compared.
   * @return The new compare request.
   * @throws NullPointerException
   *           If {@code name}, {@code attributeDescription}, or
   *           {@code assertionValue} was {@code null}.
   */
  public static CompareRequest newCompareRequest(final DN name,
      final AttributeDescription attributeDescription,
      final ByteString assertionValue) throws NullPointerException
  {
    Validator.ensureNotNull(name, attributeDescription, assertionValue);
    return new CompareRequestImpl(name, attributeDescription, assertionValue);
  }



  /**
   * Creates a new compare request using the provided distinguished name,
   * attribute name, and assertion value decoded using the default schema.
   * <p>
   * If the assertion value is not an instance of {@code ByteString} then it
   * will be converted using the {@link ByteString#valueOf(Object)} method.
   *
   * @param name
   *          The distinguished name of the entry to be compared.
   * @param attributeDescription
   *          The name of the attribute to be compared.
   * @param assertionValue
   *          The assertion value to be compared.
   * @return The new compare request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code name} or {@code attributeDescription} could not be
   *           decoded using the default schema.
   * @throws NullPointerException
   *           If {@code name}, {@code attributeDescription}, or
   *           {@code assertionValue} was {@code null}.
   */
  public static CompareRequest newCompareRequest(final String name,
      final String attributeDescription, final Object assertionValue)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    Validator.ensureNotNull(name, attributeDescription, assertionValue);
    return new CompareRequestImpl(DN.valueOf(name),
        AttributeDescription.valueOf(attributeDescription),
        ByteString.valueOf(assertionValue));
  }



  /**
   * Creates a new CRAM-MD5 SASL bind request having the provided authentication
   * ID and password.
   *
   * @param authenticationID
   *          The authentication ID of the user. The authentication ID usually
   *          has the form "dn:" immediately followed by the distinguished name
   *          of the user, or "u:" followed by a user ID string, but other forms
   *          are permitted.
   * @param password
   *          The password of the user that the client wishes to bind as. The
   *          password will be converted to a UTF-8 octet string.
   * @return The new CRAM-MD5 SASL bind request.
   * @throws NullPointerException
   *           If {@code authenticationID} or {@code password} was {@code null}.
   */
  public static CRAMMD5SASLBindRequest newCRAMMD5SASLBindRequest(
      final String authenticationID, final ByteString password)
      throws NullPointerException
  {
    return new CRAMMD5SASLBindRequestImpl(authenticationID, password);
  }



  /**
   * Creates a new delete request using the provided distinguished name.
   *
   * @param name
   *          The distinguished name of the entry to be deleted.
   * @return The new delete request.
   * @throws NullPointerException
   *           If {@code name} was {@code null}.
   */
  public static DeleteRequest newDeleteRequest(final DN name)
      throws NullPointerException
  {
    Validator.ensureNotNull(name);
    return new DeleteRequestImpl(name);
  }



  /**
   * Creates a new delete request using the provided distinguished name decoded
   * using the default schema.
   *
   * @param name
   *          The distinguished name of the entry to be deleted.
   * @return The new delete request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code name} could not be decoded using the default schema.
   * @throws NullPointerException
   *           If {@code name} was {@code null}.
   */
  public static DeleteRequest newDeleteRequest(final String name)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    Validator.ensureNotNull(name);
    return new DeleteRequestImpl(DN.valueOf(name));
  }



  /**
   * Creates a new DIGEST-MD5 SASL bind request having the provided
   * authentication ID and password, but no realm or authorization ID.
   *
   * @param authenticationID
   *          The authentication ID of the user. The authentication ID usually
   *          has the form "dn:" immediately followed by the distinguished name
   *          of the user, or "u:" followed by a user ID string, but other forms
   *          are permitted.
   * @param password
   *          The password of the user that the client wishes to bind as. The
   *          password will be converted to a UTF-8 octet string.
   * @return The new DIGEST-MD5 SASL bind request.
   * @throws NullPointerException
   *           If {@code authenticationID} or {@code password} was {@code null}.
   */
  public static DigestMD5SASLBindRequest newDigestMD5SASLBindRequest(
      final String authenticationID, final ByteString password)
      throws NullPointerException
  {
    return new DigestMD5SASLBindRequestImpl(authenticationID, password);
  }



  /**
   * Creates a new External SASL bind request with no authorization ID.
   *
   * @return The new External SASL bind request.
   */
  public static ExternalSASLBindRequest newExternalSASLBindRequest()
  {
    return new ExternalSASLBindRequestImpl();
  }



  /**
   * Creates a new generic bind request using an empty distinguished name,
   * authentication type, and authentication information.
   *
   * @param authenticationType
   *          The authentication mechanism identifier for this generic bind
   *          request.
   * @param authenticationValue
   *          The authentication information for this generic bind request in a
   *          form defined by the authentication mechanism.
   * @return The new generic bind request.
   * @throws NullPointerException
   *           If {@code authenticationValue} was {@code null}.
   */
  public static GenericBindRequest newGenericBindRequest(
      final byte authenticationType, final ByteString authenticationValue)
      throws NullPointerException
  {
    Validator.ensureNotNull(authenticationValue);
    return new GenericBindRequestImpl("", authenticationType,
        authenticationValue);
  }



  /**
   * Creates a new generic bind request using the provided name, authentication
   * type, and authentication information.
   * <p>
   * The LDAP protocol defines the Bind name to be a distinguished name, however
   * some LDAP implementations have relaxed this constraint and allow other
   * identities to be used, such as the user's email address.
   *
   * @param name
   *          The name of the Directory object that the client wishes to bind as
   *          (may be empty).
   * @param authenticationType
   *          The authentication mechanism identifier for this generic bind
   *          request.
   * @param authenticationValue
   *          The authentication information for this generic bind request in a
   *          form defined by the authentication mechanism.
   * @return The new generic bind request.
   * @throws NullPointerException
   *           If {@code name} or {@code authenticationValue} was {@code null}.
   */
  public static GenericBindRequest newGenericBindRequest(final String name,
      final byte authenticationType, final ByteString authenticationValue)
      throws NullPointerException
  {
    Validator.ensureNotNull(name, authenticationValue);
    return new GenericBindRequestImpl(name, authenticationType,
        authenticationValue);
  }



  /**
   * Creates a new generic extended request using the provided name and no
   * value.
   *
   * @param requestName
   *          The dotted-decimal representation of the unique OID corresponding
   *          to this extended request.
   * @return The new generic extended request.
   * @throws NullPointerException
   *           If {@code requestName} was {@code null}.
   */
  public static GenericExtendedRequest newGenericExtendedRequest(
      final String requestName) throws NullPointerException
  {
    Validator.ensureNotNull(requestName);
    return new GenericExtendedRequestImpl(requestName, null);
  }



  /**
   * Creates a new generic extended request using the provided name and optional
   * value.
   *
   * @param requestName
   *          The dotted-decimal representation of the unique OID corresponding
   *          to this extended request.
   * @param requestValue
   *          The content of this generic extended request in a form defined by
   *          the extended operation, or {@code null} if there is no content.
   * @return The new generic extended request.
   * @throws NullPointerException
   *           If {@code requestName} was {@code null}.
   */
  public static GenericExtendedRequest newGenericExtendedRequest(
      final String requestName, final ByteString requestValue)
      throws NullPointerException
  {
    Validator.ensureNotNull(requestName);
    return new GenericExtendedRequestImpl(requestName, requestValue);
  }



  /**
   * Creates a new GSSAPI SASL bind request having the provided authentication
   * ID and password, but no realm, KDC address, or authorization ID.
   *
   * @param authenticationID
   *          The authentication ID of the user. The authentication ID usually
   *          has the form "dn:" immediately followed by the distinguished name
   *          of the user, or "u:" followed by a user ID string, but other forms
   *          are permitted.
   * @param password
   *          The password of the user that the client wishes to bind as. The
   *          password will be converted to a UTF-8 octet string.
   * @return The new GSSAPI SASL bind request.
   * @throws NullPointerException
   *           If {@code authenticationID} or {@code password} was {@code null}.
   */
  public static GSSAPISASLBindRequest newGSSAPISASLBindRequest(
      final String authenticationID, final ByteString password)
      throws NullPointerException
  {
    return new GSSAPISASLBindRequestImpl(authenticationID, password);
  }



  /**
   * Creates a new GSSAPI SASL bind request having the provided subject, but no
   * authorization ID.
   *
   * @param subject
   *          The Kerberos subject of the user to be authenticated.
   * @return The new GSSAPI SASL bind request.
   * @throws NullPointerException
   *           If {@code subject} was {@code null}.
   */
  public static GSSAPISASLBindRequest newGSSAPISASLBindRequest(
      final Subject subject) throws NullPointerException
  {
    return new GSSAPISASLBindRequestImpl(subject);
  }



  /**
   * Creates a new modify DN request using the provided distinguished name and
   * new RDN.
   *
   * @param name
   *          The distinguished name of the entry to be renamed.
   * @param newRDN
   *          The new RDN of the entry.
   * @return The new modify DN request.
   * @throws NullPointerException
   *           If {@code name} or {@code newRDN} was {@code null}.
   */
  public static ModifyDNRequest newModifyDNRequest(final DN name,
      final RDN newRDN) throws NullPointerException
  {
    Validator.ensureNotNull(name, newRDN);
    return new ModifyDNRequestImpl(name, newRDN);
  }



  /**
   * Creates a new modify DN request using the provided distinguished name and
   * new RDN decoded using the default schema.
   *
   * @param name
   *          The distinguished name of the entry to be renamed.
   * @param newRDN
   *          The new RDN of the entry.
   * @return The new modify DN request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code name} or {@code newRDN} could not be decoded using the
   *           default schema.
   * @throws NullPointerException
   *           If {@code name} or {@code newRDN} was {@code null}.
   */
  public static ModifyDNRequest newModifyDNRequest(final String name,
      final String newRDN) throws LocalizedIllegalArgumentException,
      NullPointerException
  {
    Validator.ensureNotNull(name, newRDN);
    return new ModifyDNRequestImpl(DN.valueOf(name), RDN.valueOf(newRDN));
  }



  /**
   * Creates a new modify request using the provided distinguished name.
   *
   * @param name
   *          The distinguished name of the entry to be modified.
   * @return The new modify request.
   * @throws NullPointerException
   *           If {@code name} was {@code null}.
   */
  public static ModifyRequest newModifyRequest(final DN name)
      throws NullPointerException
  {
    Validator.ensureNotNull(name);
    return new ModifyRequestImpl(name);
  }



  /**
   * Creates a new modify request containing a list of modifications which can
   * be used to transform {@code fromEntry} into entry {@code toEntry}.
   * <p>
   * The modify request is reversible: it will contain only modifications of
   * type {@link ModificationType#ADD ADD} and {@link ModificationType#DELETE
   * DELETE}.
   * <p>
   * Finally, the modify request will use the distinguished name taken from
   * {@code fromEntry}. Moreover, this method will not check to see if both
   * {@code fromEntry} and {@code toEntry} have the same distinguished name.
   * <p>
   * This method is equivalent to:
   *
   * <pre>
   * ModifyRequest request = Entries.diffEntries(fromEntry, toEntry);
   * </pre>
   *
   * @param fromEntry
   *          The source entry.
   * @param toEntry
   *          The destination entry.
   * @return A modify request containing a list of modifications which can be
   *         used to transform {@code fromEntry} into entry {@code toEntry}.
   * @throws NullPointerException
   *           If {@code fromEntry} or {@code toEntry} were {@code null}.
   * @see Entries#diffEntries(Entry, Entry)
   */
  public static final ModifyRequest newModifyRequest(Entry fromEntry,
      Entry toEntry) throws NullPointerException
  {
    return Entries.diffEntries(fromEntry, toEntry);
  }



  /**
   * Creates a new modify request using the provided distinguished name decoded
   * using the default schema.
   *
   * @param name
   *          The distinguished name of the entry to be modified.
   * @return The new modify request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code name} could not be decoded using the default schema.
   * @throws NullPointerException
   *           If {@code name} was {@code null}.
   */
  public static ModifyRequest newModifyRequest(final String name)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    Validator.ensureNotNull(name);
    return new ModifyRequestImpl(DN.valueOf(name));
  }



  /**
   * Creates a new modify request using the provided lines of LDIF decoded using
   * the default schema.
   *
   * @param ldifLines
   *          Lines of LDIF containing a single LDIF modify change record.
   * @return The new modify request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code ldifLines} was empty, or contained invalid LDIF, or
   *           could not be decoded using the default schema.
   * @throws NullPointerException
   *           If {@code ldifLines} was {@code null} .
   */
  public static ModifyRequest newModifyRequest(final String... ldifLines)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    // LDIF change record reader is tolerant to missing change types.
    final ChangeRecord record = LDIFChangeRecordReader
        .valueOfLDIFChangeRecord(ldifLines);

    if (record instanceof ModifyRequest)
    {
      return (ModifyRequest) record;
    }
    else
    {
      // Wrong change type.
      final LocalizableMessage message = WARN_READ_LDIF_RECORD_CHANGE_RECORD_WRONG_TYPE
          .get("modify");
      throw new LocalizedIllegalArgumentException(message);
    }
  }



  /**
   * Creates a new password modify extended request, with no user identity, old
   * password, or new password.
   *
   * @return The new password modify extended request.
   */
  public static PasswordModifyExtendedRequest newPasswordModifyExtendedRequest()
  {
    return new PasswordModifyExtendedRequestImpl();
  }



  /**
   * Creates a new Plain SASL bind request having the provided authentication ID
   * and password, but no authorization ID.
   *
   * @param authenticationID
   *          The authentication ID of the user. The authentication ID usually
   *          has the form "dn:" immediately followed by the distinguished name
   *          of the user, or "u:" followed by a user ID string, but other forms
   *          are permitted.
   * @param password
   *          The password of the user that the client wishes to bind as. The
   *          password will be converted to a UTF-8 octet string.
   * @return The new Plain SASL bind request.
   * @throws NullPointerException
   *           If {@code authenticationID} or {@code password} was {@code null}.
   */
  public static PlainSASLBindRequest newPlainSASLBindRequest(
      final String authenticationID, final ByteString password)
      throws NullPointerException
  {
    return new PlainSASLBindRequestImpl(authenticationID, password);
  }



  /**
   * Creates a new search request using the provided distinguished name, scope,
   * and filter, decoded using the default schema.
   *
   * @param name
   *          The distinguished name of the base entry relative to which the
   *          search is to be performed.
   * @param scope
   *          The scope of the search.
   * @param filter
   *          The filter that defines the conditions that must be fulfilled in
   *          order for an entry to be returned.
   * @param attributeDescriptions
   *          The names of the attributes to be included with each entry.
   * @return The new search request.
   * @throws NullPointerException
   *           If the {@code name}, {@code scope}, or {@code filter} were
   *           {@code null}.
   */
  public static SearchRequest newSearchRequest(final DN name,
      final SearchScope scope, final Filter filter,
      final String... attributeDescriptions) throws NullPointerException
  {
    Validator.ensureNotNull(name, scope, filter);
    final SearchRequest request = new SearchRequestImpl(name, scope, filter);
    for (final String attributeDescription : attributeDescriptions)
    {
      request.addAttribute(attributeDescription);
    }
    return request;
  }



  /**
   * Creates a new search request using the provided distinguished name, scope,
   * and filter, decoded using the default schema.
   *
   * @param name
   *          The distinguished name of the base entry relative to which the
   *          search is to be performed.
   * @param scope
   *          The scope of the search.
   * @param filter
   *          The filter that defines the conditions that must be fulfilled in
   *          order for an entry to be returned.
   * @param attributeDescriptions
   *          The names of the attributes to be included with each entry.
   * @return The new search request.
   * @throws LocalizedIllegalArgumentException
   *           If {@code name} could not be decoded using the default schema, or
   *           if {@code filter} is not a valid LDAP string representation of a
   *           filter.
   * @throws NullPointerException
   *           If the {@code name}, {@code scope}, or {@code filter} were
   *           {@code null}.
   */
  public static SearchRequest newSearchRequest(final String name,
      final SearchScope scope, final String filter,
      final String... attributeDescriptions)
      throws LocalizedIllegalArgumentException, NullPointerException
  {
    Validator.ensureNotNull(name, scope, filter);
    final SearchRequest request = new SearchRequestImpl(DN.valueOf(name),
        scope, Filter.valueOf(filter));
    for (final String attributeDescription : attributeDescriptions)
    {
      request.addAttribute(attributeDescription);
    }
    return request;
  }



  /**
   * Creates a new simple bind request having an empty name and password
   * suitable for anonymous authentication.
   *
   * @return The new simple bind request.
   */
  public static SimpleBindRequest newSimpleBindRequest()
  {
    return new SimpleBindRequestImpl("", ByteString.empty());
  }



  /**
   * Creates a new simple bind request having the provided name and password
   * suitable for name/password authentication. The name will be decoded using
   * the default schema.
   * <p>
   * The LDAP protocol defines the Bind name to be a distinguished name, however
   * some LDAP implementations have relaxed this constraint and allow other
   * identities to be used, such as the user's email address.
   *
   * @param name
   *          The name of the Directory object that the client wishes to bind
   *          as, which may be empty.
   * @param password
   *          The password of the Directory object that the client wishes to
   *          bind as, which may be empty indicating that an unauthenticated
   *          bind is to be performed.
   * @return The new simple bind request.
   * @throws NullPointerException
   *           If {@code name} or {@code password} was {@code null}.
   */
  public static SimpleBindRequest newSimpleBindRequest(final String name,
      final char[] password) throws NullPointerException
  {
    Validator.ensureNotNull(name, password);
    return new SimpleBindRequestImpl(name, ByteString.valueOf(password));
  }



  /**
   * Creates a new start TLS extended request which will use the provided SSL
   * context.
   *
   * @param sslContext
   *          The SSLContext that should be used when installing the TLS layer.
   * @return The new start TLS extended request.
   * @throws NullPointerException
   *           If {@code sslContext} was {@code null}.
   */
  public static StartTLSExtendedRequest newStartTLSExtendedRequest(
      final SSLContext sslContext) throws NullPointerException
  {
    return new StartTLSExtendedRequestImpl(sslContext);
  }



  /**
   * Creates a new unbind request.
   *
   * @return The new unbind request.
   */
  public static UnbindRequest newUnbindRequest()
  {
    return new UnbindRequestImpl();
  }



  /**
   * Creates a new Who Am I extended request.
   *
   * @return The new Who Am I extended request.
   */
  public static WhoAmIExtendedRequest newWhoAmIExtendedRequest()
  {
    return new WhoAmIExtendedRequestImpl();
  }



  /**
   * Creates an unmodifiable abandon request of the provided request.
   *
   * @param request
   *          The abandon request to be copied.
   * @return The new abandon request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}
   */
  public static AbandonRequest unmodifiableAbandonRequest(
      final AbandonRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableAbandonRequestImpl)
    {
      return request;
    }
    return new UnmodifiableAbandonRequestImpl(request);
  }



  /**
   * Creates an unmodifiable add request of the provided request.
   *
   * @param request
   *          The add request to be copied.
   * @return The new add request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static AddRequest unmodifiableAddRequest(final AddRequest request)
      throws NullPointerException
  {
    if (request instanceof UnmodifiableAddRequestImpl)
    {
      return request;
    }
    return new UnmodifiableAddRequestImpl(request);
  }



  /**
   * Creates an unmodifiable anonymous SASL bind request of the provided
   * request.
   *
   * @param request
   *          The anonymous SASL bind request to be copied.
   * @return The new anonymous SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static AnonymousSASLBindRequest unmodifiableAnonymousSASLBindRequest(
      final AnonymousSASLBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableAnonymousSASLBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableAnonymousSASLBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable cancel extended request of the provided request.
   *
   * @param request
   *          The cancel extended request to be copied.
   * @return The new cancel extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static CancelExtendedRequest unmodifiableCancelExtendedRequest(
      final CancelExtendedRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableCancelExtendedRequestImpl)
    {
      return request;
    }
    return new UnmodifiableCancelExtendedRequestImpl(request);
  }



  /**
   * Creates an unmodifiable compare request of the provided request.
   *
   * @param request
   *          The compare request to be copied.
   * @return The new compare request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static CompareRequest unmodifiableCompareRequest(
      final CompareRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableCompareRequestImpl)
    {
      return request;
    }
    return new UnmodifiableCompareRequestImpl(request);
  }



  /**
   * Creates an unmodifiable CRAM MD5 SASL bind request of the provided request.
   *
   * @param request
   *          The CRAM MD5 SASL bind request to be copied.
   * @return The new CRAM-MD5 SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static CRAMMD5SASLBindRequest unmodifiableCRAMMD5SASLBindRequest(
      final CRAMMD5SASLBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableCRAMMD5SASLBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableCRAMMD5SASLBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable delete request of the provided request.
   *
   * @param request
   *          The add request to be copied.
   * @return The new delete request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static DeleteRequest unmodifiableDeleteRequest(
      final DeleteRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableDeleteRequestImpl)
    {
      return request;
    }
    return new UnmodifiableDeleteRequestImpl(request);
  }



  /**
   * Creates an unmodifiable digest MD5 SASL bind request of the provided
   * request.
   *
   * @param request
   *          The digest MD5 SASL bind request to be copied.
   * @return The new DIGEST-MD5 SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static DigestMD5SASLBindRequest unmodifiableDigestMD5SASLBindRequest(
      final DigestMD5SASLBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableDigestMD5SASLBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableDigestMD5SASLBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable external SASL bind request of the provided request.
   *
   * @param request
   *          The external SASL bind request to be copied.
   * @return The new External SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static ExternalSASLBindRequest unmodifiableExternalSASLBindRequest(
      final ExternalSASLBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableExternalSASLBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableExternalSASLBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable generic bind request of the provided request.
   *
   * @param request
   *          The generic bind request to be copied.
   * @return The new generic bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static GenericBindRequest unmodifiableGenericBindRequest(
      final GenericBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableGenericBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableGenericBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable generic extended request of the provided request.
   *
   * @param request
   *          The generic extended request to be copied.
   * @return The new generic extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static GenericExtendedRequest unmodifiableGenericExtendedRequest(
      GenericExtendedRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableGenericExtendedRequestImpl)
    {
      return request;
    }
    return new UnmodifiableGenericExtendedRequestImpl(request);
  }



  /**
   * Creates an unmodifiable GSSAPI SASL bind request of the provided request.
   *
   * @param request
   *          The GSSAPI SASL bind request to be copied.
   * @return The new GSSAPI SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static GSSAPISASLBindRequest unmodifiableGSSAPISASLBindRequest(
      final GSSAPISASLBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableGSSAPISASLBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableGSSAPISASLBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable modify DN request of the provided request.
   *
   * @param request
   *          The modify DN request to be copied.
   * @return The new modify DN request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static ModifyDNRequest unmodifiableModifyDNRequest(
      final ModifyDNRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableModifyDNRequestImpl)
    {
      return request;
    }
    return new UnmodifiableModifyDNRequestImpl(request);
  }



  /**
   * Creates an unmodifiable modify request of the provided request.
   *
   * @param request
   *          The modify request to be copied.
   * @return The new modify request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static ModifyRequest unmodifiableModifyRequest(
      final ModifyRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableModifyRequestImpl)
    {
      return request;
    }
    return new UnmodifiableModifyRequestImpl(request);
  }



  /**
   * Creates an unmodifiable password modify extended request of the provided
   * request.
   *
   * @param request
   *          The password modify extended request to be copied.
   * @return The new password modify extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static PasswordModifyExtendedRequest unmodifiablePasswordModifyExtendedRequest(
      final PasswordModifyExtendedRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiablePasswordModifyExtendedRequestImpl)
    {
      return request;
    }
    return new UnmodifiablePasswordModifyExtendedRequestImpl(request);
  }



  /**
   * Creates an unmodifiable plain SASL bind request of the provided request.
   *
   * @param request
   *          The plain SASL bind request to be copied.
   * @return The new Plain SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static PlainSASLBindRequest unmodifiablePlainSASLBindRequest(
      final PlainSASLBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiablePlainSASLBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiablePlainSASLBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable search request of the provided request.
   *
   * @param request
   *          The search request to be copied.
   * @return The new search request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static SearchRequest unmodifiableSearchRequest(
      final SearchRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableSearchRequestImpl)
    {
      return request;
    }
    return new UnmodifiableSearchRequestImpl(request);
  }



  /**
   * Creates an unmodifiable simple bind request of the provided request.
   *
   * @param request
   *          The simple bind request to be copied.
   * @return The new simple bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static SimpleBindRequest unmodifiableSimpleBindRequest(
      final SimpleBindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableSimpleBindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableSimpleBindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable startTLS extended request of the provided request.
   *
   * @param request
   *          The startTLS extended request to be copied.
   * @return The new start TLS extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static StartTLSExtendedRequest unmodifiableStartTLSExtendedRequest(
      final StartTLSExtendedRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableStartTLSExtendedRequestImpl)
    {
      return request;
    }
    return new UnmodifiableStartTLSExtendedRequestImpl(request);
  }



  /**
   * Creates an unmodifiable unbind request of the provided request.
   *
   * @param request
   *          The unbind request to be copied.
   * @return The new unbind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static UnbindRequest unmodifiableUnbindRequest(
      final UnbindRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableUnbindRequestImpl)
    {
      return request;
    }
    return new UnmodifiableUnbindRequestImpl(request);
  }



  /**
   * Creates an unmodifiable new Who Am I extended request of the provided
   * request.
   *
   * @param request
   *          The who Am I extended request to be copied.
   * @return The new Who Am I extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static WhoAmIExtendedRequest unmodifiableWhoAmIExtendedRequest(
      final WhoAmIExtendedRequest request) throws NullPointerException
  {
    if (request instanceof UnmodifiableWhoAmIExtendedRequestImpl)
    {
      return request;
    }
    return new UnmodifiableWhoAmIExtendedRequestImpl(request);
  }



  /**
   * Creates a new abandon request that is an exact copy of the provided
   * request.
   *
   * @param request
   *          The abandon request to be copied.
   * @return The new abandon request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}
   */
  public static AbandonRequest copyOfAbandonRequest(final AbandonRequest request)
      throws NullPointerException
  {
    return new AbandonRequestImpl(request);
  }



  /**
   * Creates a new add request that is an exact copy of the provided request.
   *
   * @param request
   *          The add request to be copied.
   * @return The new add request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static AddRequest copyOfAddRequest(final AddRequest request)
      throws NullPointerException
  {
    return new AddRequestImpl(request);
  }



  /**
   * Creates a new anonymous SASL bind request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The anonymous SASL bind request to be copied.
   * @return The new anonymous SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static AnonymousSASLBindRequest copyOfAnonymousSASLBindRequest(
      final AnonymousSASLBindRequest request) throws NullPointerException
  {
    return new AnonymousSASLBindRequestImpl(request);
  }



  /**
   * Creates a new cancel extended request that is an exact copy of the provided
   * request.
   *
   * @param request
   *          The cancel extended request to be copied.
   * @return The new cancel extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static CancelExtendedRequest copyOfCancelExtendedRequest(
      final CancelExtendedRequest request) throws NullPointerException
  {
    return new CancelExtendedRequestImpl(request);
  }



  /**
   * Creates a new compare request that is an exact copy of the provided
   * request.
   *
   * @param request
   *          The compare request to be copied.
   * @return The new compare request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static CompareRequest copyOfCompareRequest(final CompareRequest request)
      throws NullPointerException
  {
    return new CompareRequestImpl(request);
  }



  /**
   * Creates a new CRAM MD5 SASL bind request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The CRAM MD5 SASL bind request to be copied.
   * @return The new CRAM-MD5 SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static CRAMMD5SASLBindRequest copyOfCRAMMD5SASLBindRequest(
      final CRAMMD5SASLBindRequest request) throws NullPointerException
  {
    return new CRAMMD5SASLBindRequestImpl(request);
  }



  /**
   * Creates a new delete request that is an exact copy of the provided request.
   *
   * @param request
   *          The add request to be copied.
   * @return The new delete request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static DeleteRequest copyOfDeleteRequest(final DeleteRequest request)
      throws NullPointerException
  {
    return new DeleteRequestImpl(request);
  }



  /**
   * Creates a new digest MD5 SASL bind request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The digest MD5 SASL bind request to be copied.
   * @return The new DIGEST-MD5 SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static DigestMD5SASLBindRequest copyOfDigestMD5SASLBindRequest(
      final DigestMD5SASLBindRequest request) throws NullPointerException
  {
    return new DigestMD5SASLBindRequestImpl(request);
  }



  /**
   * Creates a new external SASL bind request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The external SASL bind request to be copied.
   * @return The new External SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static ExternalSASLBindRequest copyOfExternalSASLBindRequest(
      final ExternalSASLBindRequest request) throws NullPointerException
  {
    return new ExternalSASLBindRequestImpl(request);
  }



  /**
   * Creates a new generic bind request that is an exact copy of the provided
   * request.
   *
   * @param request
   *          The generic bind request to be copied.
   * @return The new generic bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static GenericBindRequest copyOfGenericBindRequest(
      final GenericBindRequest request) throws NullPointerException
  {
    return new GenericBindRequestImpl(request);
  }



  /**
   * Creates a new generic extended request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The generic extended request to be copied.
   * @return The new generic extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static GenericExtendedRequest copyOfGenericExtendedRequest(
      GenericExtendedRequest request) throws NullPointerException
  {
    return new GenericExtendedRequestImpl(request);
  }



  /**
   * Creates a new GSSAPI SASL bind request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The GSSAPI SASL bind request to be copied.
   * @return The new GSSAPI SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null}.
   */
  public static GSSAPISASLBindRequest copyOfGSSAPISASLBindRequest(
      final GSSAPISASLBindRequest request) throws NullPointerException
  {
    return new GSSAPISASLBindRequestImpl(request);
  }



  /**
   * Creates a new modify DN request that is an exact copy of the provided
   * request.
   *
   * @param request
   *          The modify DN request to be copied.
   * @return The new modify DN request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static ModifyDNRequest copyOfModifyDNRequest(
      final ModifyDNRequest request) throws NullPointerException
  {
    return new ModifyDNRequestImpl(request);
  }



  /**
   * Creates a new modify request that is an exact copy of the provided request.
   *
   * @param request
   *          The modify request to be copied.
   * @return The new modify request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static ModifyRequest copyOfModifyRequest(final ModifyRequest request)
      throws NullPointerException
  {
    return new ModifyRequestImpl(request);
  }



  /**
   * Creates a new password modify extended request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The password modify extended request to be copied.
   * @return The new password modify extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static PasswordModifyExtendedRequest copyOfPasswordModifyExtendedRequest(
      final PasswordModifyExtendedRequest request) throws NullPointerException
  {
    return new PasswordModifyExtendedRequestImpl(request);
  }



  /**
   * Creates a new plain SASL bind request that is an exact copy of the provided
   * request.
   *
   * @param request
   *          The plain SASL bind request to be copied.
   * @return The new Plain SASL bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static PlainSASLBindRequest copyOfPlainSASLBindRequest(
      final PlainSASLBindRequest request) throws NullPointerException
  {
    return new PlainSASLBindRequestImpl(request);
  }



  /**
   * Creates a new search request that is an exact copy of the provided request.
   *
   * @param request
   *          The search request to be copied.
   * @return The new search request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static SearchRequest copyOfSearchRequest(final SearchRequest request)
      throws NullPointerException
  {
    return new SearchRequestImpl(request);
  }



  /**
   * Creates a new simple bind request that is an exact copy of the provided
   * request.
   *
   * @param request
   *          The simple bind request to be copied.
   * @return The new simple bind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static SimpleBindRequest copyOfSimpleBindRequest(
      final SimpleBindRequest request) throws NullPointerException
  {
    return new SimpleBindRequestImpl(request);
  }



  /**
   * Creates a new startTLS extended request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The startTLS extended request to be copied.
   * @return The new start TLS extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static StartTLSExtendedRequest copyOfStartTLSExtendedRequest(
      final StartTLSExtendedRequest request) throws NullPointerException
  {
    return new StartTLSExtendedRequestImpl(request);
  }



  /**
   * Creates a new unbind request that is an exact copy of the provided request.
   *
   * @param request
   *          The unbind request to be copied.
   * @return The new unbind request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static UnbindRequest copyOfUnbindRequest(final UnbindRequest request)
      throws NullPointerException
  {
    return new UnbindRequestImpl(request);
  }



  /**
   * Creates a new Who Am I extended request that is an exact copy of the
   * provided request.
   *
   * @param request
   *          The who Am I extended request to be copied.
   * @return The new Who Am I extended request.
   * @throws NullPointerException
   *           If {@code request} was {@code null} .
   */
  public static WhoAmIExtendedRequest copyOfWhoAmIExtendedRequest(
      final WhoAmIExtendedRequest request) throws NullPointerException
  {
    return new WhoAmIExtendedRequestImpl(request);
  }



  private Requests()
  {
    // Prevent instantiation.
  }
}