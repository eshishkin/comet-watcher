db.getSiblingDB('test_db').createUser({
    user: 'test_user',
    pwd: 'test_password',
    roles: [ { role: 'readWrite', db: 'test_db' } ]
})