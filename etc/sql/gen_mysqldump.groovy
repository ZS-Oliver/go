/**
 * 读取 db.properties, 生成 mysqldump命令
<<<<<<< HEAD
 *
 * 生成的备份只有建表信息
=======
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
 */
import java.nio.file.Paths

prop = new Properties()
<<<<<<< HEAD
prop.load(new FileReader(Paths.get('..', '..', 'src', 'main', 'resources-dev', 'db.properties').toFile()))
=======
prop.load(new FileReader(Paths.get('..', '..', 'src', 'main', 'resources', 'db.properties').toFile()))
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2

urlParts = prop.getProperty('db.url').split('/')

cmd = "mysqldump -h${urlParts[2].substring(0, urlParts[2].indexOf(':'))} -u${prop.getProperty('db.username')} -p${prop.getProperty('db.password')} -d ${urlParts[3]} > db.sql"

println cmd
