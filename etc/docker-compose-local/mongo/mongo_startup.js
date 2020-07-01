db.getSiblingDB('comet_watcher_db').createUser({
    user: 'comet_watcher_usr',
    pwd: 'comet_watcher_pwd',
    roles: [ { role: 'readWrite', db: 'comet_watcher_db' } ]
})