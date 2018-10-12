/**
 * 读取 db.properties, 生成 mysqldump命令
 */
import java.nio.file.Paths

prop = new Properties()
prop.load(new FileReader(Paths.get('..', '..', 'src', 'main', 'resources', 'db.properties').toFile()))

urlParts = prop.getProperty('db.url').split('/')

cmd = "mysqldump -h${urlParts[2].substring(0, urlParts[2].indexOf(':'))} -u${prop.getProperty('db.username')} -p${prop.getProperty('db.password')} -d ${urlParts[3]} > db.sql"

println cmd
