Scenario: User calls to the main page

Given user call the Main page with the url: 'http://localhost'
When the user click in the button: 'Human vs Computer'
Then the score 1 title is: 'You' and the score 2 title is: 'Computer'
Then the value of score 1 is: '0': and the value of score 2 is: '0'

Scenario: User calls to an unknown page

Given user call the Main page with the url: 'http://localhost/unknown'
When the user click in the button: 'Human vs Computer'
Then the score 1 title is: 'You' and the score 2 title is: 'Computer'
Then the value of score 1 is: '0': and the value of score 2 is: '0'

Scenario: User calls to a rest service without accept application/json header

Given a 'GET' user call the url: 'http://localhost/rest/configuration'
Then the response code is: '404'
Then the response message is: 'Resource not found: /rest/configuration'
Then the content type is: 'text/plain'

Scenario: Test not allowed http method calls

Given a '<method>' call to the rest service: 'http://localhost/rest/configuration' returns a 'not null' response
Then the response code is: '405'
Then the response message with example is: 'Method Not Allowed: ' '<method>'
Then the content type is: 'text/plain'

Examples:
| method    |
| PUT       |
| POST      |
| DELETE    |
| OPTIONS   |

Scenario: Test call rest configuration service endpoint

Given a 'GET' call to the rest service: 'http://localhost/rest/configuration' returns a 'not null' response
Then the response code is: '200'
Then the response message is: 'OK'
Then the content type is: 'application/json'
Then the json message is: '{"maxScore":"7","noteTimeout":"1e3","wellcomeMessages":"5"}'

Scenario: Test call rest service endpoints for game move

Given a 'GET' call to the rest service: '<endpoint>' returns a 'valid' response
Then the response code is: '200'
Then the response message is: 'OK'
Then the content type is: 'application/json'

Examples:
| endpoint                        |
| http://localhost/rest/rock      |
| http://localhost/rest/paper     |
| http://localhost/rest/scissors  |
| http://localhost/rest/computer  |


Scenario: Test call for a invalid rest service endpoint

Given a 'GET' call to the rest service: 'http://localhost/rest/invalid' returns a 'not null' response
Then the response code is: '200'
Then the response message is: 'OK'
Then the content type is: 'application/json'
Then the json message is: '{"message":"Invalid endpoint: /rest/invalid"}'