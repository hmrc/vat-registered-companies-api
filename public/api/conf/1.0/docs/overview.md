This API enables your application to check if a VAT number is registered, to view the name and address of the business that has registered a VAT number and to get a consultation number with which you can prove to HMRC that you have doen this check.

The Check a UK VAT number API works in two discrete modes.  It is recommended that your application consumes the Check a UK VAT number API in one of the following modes:


## Simple unverified check

This mode will not provide a consultation number.

This mode is actioned via the following endpoint:

GET /organisations/vat/check-vat-number/lookup/{targetVatNumber} - returns the name and address of the registered business if it exists 

## Verified check

This mode with provide a consultation number. 

This mode is actioned via the following endpoint:
GET /organisations/vat/check-vat-number/lookup/{targetVatNumber}/{requesterVatNumber} - returns the name and address of the registered business and a consultation number if the business exists


