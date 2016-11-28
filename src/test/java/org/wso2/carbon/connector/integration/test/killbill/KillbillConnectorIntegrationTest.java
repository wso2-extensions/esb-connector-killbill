/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.integration.test.killbill;

import org.apache.axiom.om.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KillbillConnectorIntegrationTest extends ConnectorIntegrationTestBase {

    private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();
    private Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        init("killbill-connector-1.0.0-SNAPSHOT");
        esbRequestHeadersMap.put("Accept-Charset", "UTF-8");
        esbRequestHeadersMap.put("Content-Type", "application/json");
        apiRequestHeadersMap.putAll(esbRequestHeadersMap);
        String authString = connectorProperties.getProperty("username") + ":" + connectorProperties.getProperty("password");
        String authorizationHeader = "Basic " + Base64.encode(authString.getBytes());
        apiRequestHeadersMap.put("Authorization", authorizationHeader);
        apiRequestHeadersMap.put("X-Killbill-ApiKey", connectorProperties.getProperty("apiKey"));
        apiRequestHeadersMap.put("X-Killbill-ApiSecret", connectorProperties.getProperty("apiSecret"));

    }

    /**
     * Positive test case for createAccount method with mandatory parameters.
     */
    @Test(priority = 1, description = "Killbill {createAccount} integration test with mandatory parameters.")
    public void testCreateAccountWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:createAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createAccount_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts?externalKey=" + connectorProperties.getProperty("externalKey");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);
        String accountId = apiRestResponse.getBody().getString("accountId");
        connectorProperties.put("accountId", accountId);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(connectorProperties.getProperty("email"), apiRestResponse.getBody().getString("email"));
    }

    /**
     * Positive test case for updateAccount method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testCreateAccountWithMandatoryParameters"}, description = "Killbill {updateAccount} integration test with mandatory parameters.")
    public void testUpdateAccountWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:updateAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_updateAccount_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts?externalKey=" + connectorProperties.getProperty("externalKey");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(connectorProperties.getProperty("updateEmail"), apiRestResponse.getBody().getString("email"));
    }

    /**
     * Positive test case for getAccountByExternalKey method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testUpdateAccountWithMandatoryParameters"}, description = "Killbill {getAccountByExternalKey} integration test with mandatory parameters.")
    public void testGetAccountByExternalKeyWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getAccountByExternalKey");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getAccountByExternalKey_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts?externalKey=" + connectorProperties.getProperty("externalKey");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("email"), apiRestResponse.getBody().getString("email"));
    }

    /**
     * Positive test case for getAccountByExternalKey method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetAccountByExternalKeyWithMandatoryParameters"}, description = "Killbill {getAccountByExternalKey} integration test with optional parameters.")
    public void testGetAccountByExternalKeyWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getAccountByExternalKey");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getAccountByExternalKey_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts?externalKey=" + connectorProperties.getProperty("externalKey")+"&audit=Full";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().getString("email"), apiRestResponse.getBody().getString("email"));
    }

    /**
     * Positive test case for getPaymentsOfAccount method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetAccountByExternalKeyWithOptionalParameters"}, description = "Killbill {getPaymentsOfAccount} integration test with mandatory parameters.")
    public void testGetPaymentsOfAccountWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getPaymentsOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPaymentsOfAccount_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/payments";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getPaymentsOfAccount method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetPaymentsOfAccountWithMandatoryParameters"}, description = "Killbill {getPaymentsOfAccount} integration test with optional parameters.")
    public void testGetPaymentsOfAccountWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getPaymentsOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPaymentsOfAccount_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/payments?audit=Full&withPluginInfo=true";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getPaymentMethodsOfAccount method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetPaymentsOfAccountWithOptionalParameters"}, description = "Killbill {getPaymentMethodsOfAccount} integration test with mandatory parameters.")
    public void testGetPaymentMethodsOfAccountWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getPaymentMethodsOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPaymentMethodsOfAccount_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/paymentMethods";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getPaymentMethodsOfAccount method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetPaymentMethodsOfAccountWithMandatoryParameters"}, description = "Killbill {getPaymentMethodsOfAccount} integration test with optional parameters.")
    public void testGetPaymentMethodsOfAccountWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getPaymentMethodsOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPaymentMethodsOfAccount_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/paymentMethods?audit=Full&withPluginInfo=true";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getInvoicePaymentsOfAccount method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetPaymentMethodsOfAccountWithOptionalParameters"}, description = "Killbill {getInvoicePaymentsOfAccount} integration test with mandatory parameters.")
    public void testGetInvoicePaymentsOfAccountWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getInvoicePaymentsOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getInvoicePaymentsOfAccount_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/invoicePayments";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getInvoicePaymentsOfAccount method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetInvoicePaymentsOfAccountWithMandatoryParameters"}, description = "Killbill {getInvoicePaymentsOfAccount} integration test with optional parameters.")
    public void testGetInvoicePaymentsOfAccountWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getInvoicePaymentsOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getInvoicePaymentsOfAccount_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/invoicePayments?audit=Full&withPluginInfo=true";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for searchAccount method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetInvoicePaymentsOfAccountWithOptionalParameters"}, description = "Killbill {searchAccount} integration test with mandatory parameters.")
    public void testSearchAccountWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:searchAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_searchAccount_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/search/" + connectorProperties.getProperty("searchKey");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for searchAccount method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testSearchAccountWithMandatoryParameters"}, description = "Killbill {searchAccount} integration test with optional parameters.")
    public void testSearchAccountWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:searchAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_searchAccount_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/search/" + connectorProperties.getProperty("searchKey")+"?audit=Full&limit=10";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }
    /**
     * Positive test case for addPaymentMethod method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testSearchAccountWithOptionalParameters"}, description = "Killbill {addPaymentMethod} integration test with mandatory parameters.")
    public void testaddPaymentMethodWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:addPaymentMethod");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_addPaymentMethod_mandatory.json");
        String paymentMethodId = esbRestResponse.getHeadersMap().get("Location").toString();
        paymentMethodId = paymentMethodId.substring(paymentMethodId.lastIndexOf("/") + 1, paymentMethodId.length() - 1);
        connectorProperties.put("paymentMethodId", paymentMethodId);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
    }

    /**
     * Positive test case for createSubscription method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testaddPaymentMethodWithMandatoryParameters"}, description = "Killbill {createSubscription} integration test with mandatory parameters.")
    public void testCreateSubscriptionWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:createSubscription");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createSubscription_mandatory.json");
        String subscriptionId = esbRestResponse.getHeadersMap().get("Location").toString();
        subscriptionId = subscriptionId.substring(subscriptionId.lastIndexOf("/") + 1, subscriptionId.length() - 1);
        connectorProperties.put("subscriptionId", subscriptionId);
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/subscriptions/" + connectorProperties.getProperty("subscriptionId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(connectorProperties.getProperty("subscriptionExternalKey"), apiRestResponse.getBody().getString("externalKey"));
    }

    /**
     * Positive test case for createPayment method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testCreateSubscriptionWithMandatoryParameters"}, description = "Killbill {createPayment} integration test with mandatory parameters.")
    public void testCreatePaymentWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:createPayment");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_createPayment_mandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
    }

    /**
     * Positive test case for getPaymentMethodById method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testCreatePaymentWithMandatoryParameters"}, description = "Killbill {getPaymentMethodById} integration test with mandatory parameters.")
    public void testGetPaymentMethodByIdWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getPaymentMethodById");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPaymentMethodById_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/paymentMethods/" + connectorProperties.getProperty("paymentMethodId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getPaymentMethodById method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetPaymentMethodByIdWithMandatoryParameters"}, description = "Killbill {getPaymentMethodById} integration test with optional parameters.")
    public void testGetPaymentMethodByIdWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getPaymentMethodById");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getPaymentMethodById_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/paymentMethods/" + connectorProperties.getProperty("paymentMethodId")+"?audit=Full";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for searchPaymentMethod method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetPaymentMethodByIdWithOptionalParameters"}, description = "Killbill {searchPaymentMethod} integration test with mandatory parameters.")
    public void testSearchPaymentMethodWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:searchPaymentMethod");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_searchPaymentMethod_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/paymentMethods/search/" + connectorProperties.getProperty("paymentMethodId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for searchPaymentMethod method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testSearchPaymentMethodWithMandatoryParameters"}, description = "Killbill {searchPaymentMethod} integration test with optional parameters.")
    public void testSearchPaymentMethodWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:searchPaymentMethod");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_searchPaymentMethod_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/paymentMethods/search/" + connectorProperties.getProperty("paymentMethodId")+"?limit=10&audit=Full";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getUsageOfSubscription method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testSearchPaymentMethodWithOptionalParameters"}, description = "Killbill {getUsageOfSubscription} integration test with mandatory parameters.")
    public void testGetUsageOfSubscriptionWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getUsageOfSubscription");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getUsageOfSubscription_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/usages/" + connectorProperties.getProperty("subscriptionId")+"?startDate="
                +connectorProperties.getProperty("startDate")+"&endDate="+connectorProperties.getProperty("endDate");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for updateSubscription method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetUsageOfSubscriptionWithMandatoryParameters"}, description = "Killbill {updateSubscription} integration test with mandatory parameters.")
    public void testUpdateSubscriptionWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:updateSubscription");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_updateSubscription_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/subscriptions/" + connectorProperties.getProperty("subscriptionId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(connectorProperties.getProperty("updateProductName"), apiRestResponse.getBody().getString("productName"));
    }


    /**
     * Positive test case for getSubscriptionById method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testUpdateSubscriptionWithMandatoryParameters"}, description = "Killbill {getSubscriptionById} integration test with mandatory parameters.")
    public void testGetSubscriptionByIdWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getSubscriptionById");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getSubscriptionById_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/subscriptions/" + connectorProperties.getProperty("subscriptionId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for deleteSubscription method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetSubscriptionByIdWithMandatoryParameters"}, description = "Killbill {deleteSubscription} integration test with mandatory parameters.")
    public void testDeleteSubscriptionWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:deleteSubscription");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deleteSubscription_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for deletePaymentMethod method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testDeleteSubscriptionWithMandatoryParameters"}, description = "Killbill {deletePaymentMethod} integration test with mandatory parameters.")
    public void testDeletePaymentMethodWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:deletePaymentMethod");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_deletePaymentMethod_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }


    /**
     * Positive test case for searchInvoice method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testDeletePaymentMethodWithMandatoryParameters"}, description = "Killbill {searchInvoice} integration test with mandatory parameters.")
    public void testSearchInvoiceWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:searchInvoice");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_searchInvoice_mandatory.json");

        String response=esbRestResponse.getBody().toString();
        response = response.substring(response.indexOf("["), response.lastIndexOf("]") + 1);
        JSONArray array = new JSONArray(response.replace("\\",""));
        String invoiceId = array.getJSONObject(0).getString("invoiceId").toString();
        connectorProperties.put("invoiceId", invoiceId);
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/invoices/search/" + connectorProperties.getProperty("invoiceSearchKey");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for searchInvoice method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testSearchInvoiceWithMandatoryParameters"}, description = "Killbill {searchInvoice} integration test with optional parameters.")
    public void testSearchInvoiceWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:searchInvoice");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_searchInvoice_optional.json");

        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/invoices/search/" + connectorProperties.getProperty("invoiceSearchKey")+"?limit=10&audit=Full";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getInvoicesOfAccount method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testSearchInvoiceWithOptionalParameters"}, description = "Killbill {getInvoicesOfAccount} integration test with mandatory parameters.")
    public void testGetInvoicesOfAccountWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getInvoicesOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getInvoicesOfAccount_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/invoices";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getInvoicesOfAccount method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetInvoicesOfAccountWithMandatoryParameters"}, description = "Killbill {getInvoicesOfAccount} integration test with optional parameters.")
    public void testGetInvoicesOfAccountWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getInvoicesOfAccount");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getInvoicesOfAccount_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId") + "/invoices?audit=Full&withItems=true";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getInvoiceById method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetInvoicesOfAccountWithOptionalParameters"}, description = "Killbill {getInvoiceById} integration test with mandatory parameters.")
    public void testGetInvoiceByIdWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getInvoiceById");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getInvoiceById_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/invoices/" + connectorProperties.getProperty("invoiceId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getInvoiceById method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetInvoiceByIdWithMandatoryParameters"}, description = "Killbill {getInvoiceById} integration test with optional parameters.")
    public void testGetInvoiceByIdWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getInvoiceById");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getInvoiceById_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/invoices/" + connectorProperties.getProperty("invoiceId")+"?audit=Full&withItems=true";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }
    /**
     * Positive test case for getAccountById method with mandatory parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetInvoiceByIdWithOptionalParameters"}, description = "Killbill {getAccountById} integration test with mandatory parameters.")
    public void testGetAccountByIdWithMandatoryParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getAccountById");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getAccountById_mandatory.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getAccountById method with optional parameters.
     */
    @Test(priority = 1, dependsOnMethods = {"testGetAccountByIdWithMandatoryParameters"}, description = "Killbill {getAccountById} integration test with optional parameters.")
    public void testGetAccountByIdWithOptionalParameters() throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:getAccountById");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "esb_getAccountById_optional.json");
        String apiUrl = connectorProperties.getProperty("apiUrl") + "/" + connectorProperties.getProperty("apiVersion") + "/kb/accounts/" + connectorProperties.getProperty("accountId")+"?audit=Full";
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiUrl, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

}
