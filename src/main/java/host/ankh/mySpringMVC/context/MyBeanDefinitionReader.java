package host.ankh.mySpringMVC.context;

import host.ankh.mySpringMVC.core.MyBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 对配置文件进行查找, 读取, 解析
 * @author ankh
 * @created at 2022-04-10 16:07
 */
public class MyBeanDefinitionReader {
    private List<String> registryBeanClasses = new ArrayList<>();
    private Properties config = new Properties();

    // 规定application.properties 里的key
    private final String SCAN_PACKAGE = "scanPackage";

    public MyBeanDefinitionReader(String... locations) {
        // 通过 URI 定位找到对应的配置文件, 然后转化为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != is) {
                // 如果文件流不为空, 记得关闭
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        // 把全类名转化成文件路径, 注意这里需要转移'.'
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                // 如果是个目录, 就递归扫描
                doScanner(scanPackage + "." + file.getName());
            } else {
                // 如果是文件, 就加到registryBeanClasses里边
                if (!file.getName().endsWith(".class")) continue;
                String className = scanPackage + "." + file.getName().replace(".class", "");
                registryBeanClasses.add(className);
            }
        }
    }

    public Properties getConfig() {
        return config;
    }

    /**
     * 将扫描到的所有class文件的配置信息转化为 BeanDefinition 对象, 便于以后的 IOC 操作
     * @return
     */
    public List<MyBeanDefinition> loadBeanDefinitions() {
        ArrayList<MyBeanDefinition> res = new ArrayList<>();

        try {
            for (String className : registryBeanClasses) {
                // 通过全类名加载 class 文件
                Class<?> beanClass = Class.forName(className);

                if (beanClass.isInterface()) continue;

                res.add(
                        doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName())
                );

                // 如果该配置类实现了接口
                // ❓把接口作为工厂Bean转化为 BeanDefinition
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    res.add(
                            doCreateBeanDefinition(i.getName(), beanClass.getName())
                    );
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 将配置信息转化为 BeanDefinition
     * @param factoryBeanName
     * @param beanClassName
     * @return
     */
    private MyBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        MyBeanDefinition beanDefinition = new MyBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**
     * 将类名首字母改成小写, 用来作为 BeanName
     * @param originalName
     * @return
     */
    private String toLowerFirstCase(String originalName) {
        char[] chars = originalName.toCharArray();

        // ASCII 码加32就是小写了
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }
}
