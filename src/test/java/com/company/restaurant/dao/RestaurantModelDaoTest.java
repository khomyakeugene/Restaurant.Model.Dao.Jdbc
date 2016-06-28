package com.company.restaurant.dao;

import com.company.restaurant.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Yevhen on 08.06.2016.
 */
public abstract class RestaurantModelDaoTest {
    private final static String DUPLICATE_KEY_VALUE_VIOLATES_MESSAGE = "duplicate key value violates";

    private static JobPositionDao jobPositionDao;
    private static EmployeeDao employeeDao;
    private static MenuDao menuDao;
    private static TableDao tableDao;
    private static CourseDao courseDao;
    private static CourseCategoryDao courseCategoryDao;
    private static CookedCourseDao cookedCourseDao;
    private static StateDao stateDao;
    private static StateGraphDao stateGraphDao;
    private static OrderDao orderDao;
    private static IngredientDao ingredientDao;
    private static PortionDao portionDao;
    private static WarehouseDao warehouseDao;

    private static Employee employee() {
        return employeeDao.findAllEmployees().get(0);
    }

    private static int jobPositionId() {
        return jobPositionDao.findAllJobPositions().get(0).getId();
    }

    private static int courseCategoryId() {
        return courseCategoryDao.findAllCourseCategories().get(0).getId();
    }

    private static int employeeId() {
        return employee().getEmployeeId();
    }

    private static int tableId() {
        return tableDao.findAllTables().get(0).getTableId();
    }

    private static int lastTableId() {
        List<Table> tableList = tableDao.findAllTables();

        return tableList.get(tableList.size()-1).getTableId();
    }


    protected static void initDataSource(String configLocation) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);

        menuDao = applicationContext.getBean(MenuDao.class);
        tableDao = applicationContext.getBean(TableDao.class);
        employeeDao = applicationContext.getBean(EmployeeDao.class);
        jobPositionDao = applicationContext.getBean(JobPositionDao.class);
        courseDao = applicationContext.getBean(CourseDao.class);
        courseCategoryDao = applicationContext.getBean(CourseCategoryDao.class);
        cookedCourseDao = applicationContext.getBean(CookedCourseDao.class);
        stateDao = applicationContext.getBean(StateDao.class);
        stateGraphDao = applicationContext.getBean(StateGraphDao.class);
        orderDao = applicationContext.getBean(OrderDao.class);
        ingredientDao = applicationContext.getBean(IngredientDao.class);
        portionDao = applicationContext.getBean(PortionDao.class);
        warehouseDao = applicationContext.getBean(WarehouseDao.class);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        initDataSource(null); // intentionally, to generate exception if use this code directly
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test(timeout = 2000)
    public void addFindDelJobPosition() throws Exception {
        String name = Util.getRandomString();
        JobPosition jobPosition = jobPositionDao.addJobPosition(name);

        assertTrue(jobPosition.equals(jobPositionDao.findJobPositionByName(jobPosition.getName())));
        assertTrue(jobPosition.equals(jobPositionDao.findJobPositionById(jobPosition.getId())));

        jobPositionDao.delJobPosition(name);
        assertTrue(jobPositionDao.findJobPositionByName(name) == null);
        // Test delete of non-existent data
        jobPositionDao.delJobPosition(name);

        for (JobPosition jP : jobPositionDao.findAllJobPositions()) {
            System.out.println("Job position Id :" + jP.getId() +
                    ", Job position name :" + jP.getName());
        }
    }

    @Test(timeout = 2000)
    public void addFindDelEmployeeTest() throws Exception {
        String firstName = Util.getRandomString();
        String secondName = Util.getRandomString();
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setSecondName(secondName);
        employee.setPhoneNumber(Util.getRandomString());
        employee.setSalary(Util.getRandomFloat());
        employee.setJobPosition(jobPositionDao.findJobPositionById(jobPositionId()));

        employee = employeeDao.addEmployee(employee);
        int employeeId = employee.getEmployeeId();

        // Select test <employee> and check
        assertTrue(employee.equals(employeeDao.findEmployeeByFirstName(firstName).get(0)));
        assertTrue(employee.equals(employeeDao.findEmployeeBySecondName(secondName).get(0)));
        assertTrue(employee.equals(employeeDao.findEmployeeByFirstAndSecondName(firstName, secondName).get(0)));
        assertTrue(employee.equals(employeeDao.findEmployeeById(employeeId)));

        employeeDao.delEmployee(employee);
        assertTrue(employeeDao.findEmployeeById(employeeId) == null);
    }

    @Test(timeout = 2000)
    public void addFindDelCourseCategoryTest() throws Exception {
        String name = Util.getRandomString();
        CourseCategory courseCategory = courseCategoryDao.addCourseCategory(name);

        assertTrue(courseCategory.equals(courseCategoryDao.findCourseCategoryByName(courseCategory.getName())));
        assertTrue(courseCategory.equals(courseCategoryDao.findCourseCategoryById(courseCategory.getId())));

        courseCategoryDao.delCourseCategory(name);
        assertTrue(courseCategoryDao.findCourseCategoryByName(name) == null);
        // Test delete of non-existent data
        courseCategoryDao.delCourseCategory(name);
    }

    @Test(timeout = 2000)
    public void addFindDelCourseTest() throws Exception {
        CourseCategory courseCategory = new CourseCategory();
        courseCategory.setId(courseCategoryId());

        Course course = new Course();
        String name = Util.getRandomString();
        course.setName(name);
        course.setWeight(Util.getRandomFloat());
        course.setCost(Util.getRandomFloat());
        course.setCourseCategory(courseCategory);
        course = courseDao.addCourse(course);

        assertTrue(course.equals(courseDao.findCourseByName(course.getName())));
        assertTrue(course.equals(courseDao.findCourseById(course.getCourseId())));

        courseDao.delCourse(name);
        assertTrue(courseDao.findCourseByName(name) == null);
        // Test delete by "the whole object"
        course = courseDao.addCourse(course);
        assertTrue(course.equals(courseDao.findCourseByName(name)));
        courseDao.delCourse(course);
        assertTrue(courseDao.findCourseByName(name) == null);
        // Test delete of non-existent data
        courseDao.delCourse(name);

        // Whole course list
        for (Course course1 : courseDao.findAllCourses()) {
            System.out.println("Course: id: " + course1.getCourseId() + ", name: " + course1.getName());
        }
    }

    @Test(timeout = 2000)
    public void addFindDelMenuTest() throws Exception {
        String name = Util.getRandomString();
        Menu menu = menuDao.addMenu(name);

        assertTrue(menu.equals(menuDao.findMenuByName(name)));
        assertTrue(menu.equals(menuDao.findMenuById(menu.getId())));

        // Courses in menu ----------------------------
        Course course1 = new Course();
        course1.setName(Util.getRandomString());
        course1.setWeight(Util.getRandomFloat());
        course1.setCost(Util.getRandomFloat());
        course1.setCourseCategory(courseCategoryDao.findCourseCategoryById(courseCategoryId()));
        course1 = courseDao.addCourse(course1);

        Course course2 = new Course();
        course2.setName(Util.getRandomString());
        course2.setWeight(Util.getRandomFloat());
        course2.setCost(Util.getRandomFloat());
        course2.setCourseCategory(courseCategoryDao.findCourseCategoryById(courseCategoryId()));
        course2 = courseDao.addCourse(course2);

        menuDao.addCourseToMenu(menu, course1);
        menuDao.addCourseToMenu(menu, course2);

        assertTrue(course1.equals(menuDao.findMenuCourseByCourseId(menu, course1.getCourseId())));

        for (Course course : menuDao.findMenuCourses(menu)) {
            menuDao.findMenuCourseByCourseId(menu, course.getCourseId());
            System.out.println(course.getName() + ": " + course.getCourseCategory().getName());
        }

        menuDao.delCourseFromMenu(menu, course1);
        menuDao.delCourseFromMenu(menu, course2);

        assertTrue(menuDao.findMenuCourseByCourseId(menu, course1.getCourseId()) == null);

        courseDao.delCourse(course1);
        courseDao.delCourse(course2);
        // ----------------------------

        for (Menu m : menuDao.findAllMenus()) {
            System.out.println("menu_id: " + m.getId() + ", name: " + m.getName());
        }

        menuDao.delMenu(name);
        assertTrue(menuDao.findMenuByName(name) == null);
    }

    @Test(timeout = 2000)
    public void addFindDelTableTest() throws Exception {
        Table table = new Table();
        table.setDescription(Util.getRandomString());
        boolean tableWasNotAdded = true;
        do {
            try {
                table.setNumber(tableDao.findTableById(lastTableId()).getNumber() + Util.getRandomInteger());
                table = tableDao.addTable(table);
                tableWasNotAdded = false;
            } catch (RuntimeException e) {
                //  Error "duplicate key value violates unique constraint "ak_u_table_number_table"" could be generated
                if (!e.getMessage().contains(DUPLICATE_KEY_VALUE_VIOLATES_MESSAGE)) {
                    throw new RuntimeException(e);

                }
            }
        } while (tableWasNotAdded);

        assertTrue(table.equals(tableDao.findTableByNumber(table.getNumber())));
        assertTrue(table.equals(tableDao.findTableById(table.getTableId())));

        // Whole table list
        for (Table table1 : tableDao.findAllTables()) {
            System.out.println("Table: id: " + table1.getId() + ", name: " + table1.getName() +
                    ", number: " + table1.getNumber());
        }

        tableDao.delTable(table);
        assertTrue(tableDao.findTableByNumber(table.getNumber()) == null);
    }

    @Test(timeout = 2000)
    public void addFindDelOrderTest() throws Exception {
        Order order = new Order();
        order.setOrderNumber(Util.getRandomString());
        order.setWaiter(employeeDao.findEmployeeById(employeeId()));
        order.setTable(tableDao.findTableById(tableId()));
        order.setState(stateDao.findStateByType("A"));
        order = orderDao.addOrder(order);
        int orderId = order.getOrderId();

        Order findOrder = orderDao.findOrderById(order.getOrderId());

        assertTrue(order.equals(findOrder));

        // Courses in order ----------------------------
        Course course1 = new Course();
        course1.setName(Util.getRandomString());
        course1.setWeight(Util.getRandomFloat());
        course1.setCost(Util.getRandomFloat());
        course1.setCourseCategory(courseCategoryDao.findCourseCategoryById(courseCategoryId()));
        course1 = courseDao.addCourse(course1);

        Course course2 = new Course();
        course2.setName(Util.getRandomString());
        course2.setWeight(Util.getRandomFloat());
        course2.setCost(Util.getRandomFloat());
        course2.setCourseCategory(courseCategoryDao.findCourseCategoryById(courseCategoryId()));
        course2 = courseDao.addCourse(course2);

        orderDao.addCourseToOrder(order, course1);
        orderDao.addCourseToOrder(order, course1);
        orderDao.addCourseToOrder(order, course2);

        for (Course course : orderDao.findOrderCourses(order)) {
            orderDao.findOrderCourseByCourseId(order, course.getCourseId());
            System.out.println(course.getName() + " : " + course.getCost());
        }

        assertTrue(course1.equals(orderDao.findOrderCourseByCourseId(order, course1.getCourseId())));

        orderDao.takeCourseFromOrder(order, course1);
        orderDao.takeCourseFromOrder(order, course1);
        orderDao.takeCourseFromOrder(order, course2);

        assertTrue(orderDao.findOrderCourseByCourseId(order, course1.getCourseId()) == null);

        courseDao.delCourse(course1);
        courseDao.delCourse(course2);
        // ----------------------------

        for (Order o : orderDao.findAllOrders()) {
            System.out.println("Order id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        for (Order o : orderDao.findAllOrders("A")) {
            System.out.println("Open order id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        for (Order o : orderDao.findAllOrders("B")) {
            System.out.println("Closed order id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        orderDao.delOrder(order);
        assertTrue(orderDao.findOrderById(orderId) == null);

        for (StateGraph stateGraph : stateGraphDao.findEntityStateGraph(orderDao.orderEntityName())) {
            System.out.println("stateGraph: entityName: " + stateGraph.getEntityName() +
                    ", initStateType: " + stateGraph.getInitStateType() +
                    ", finiteStateType: " + stateGraph.getFiniteStateType() +
                    ", comment: " + stateGraph.getComment());
        }
    }

    @Test(timeout = 2000)
    public void addDelCookedCourse() throws Exception {
        Course testCourse = new Course();
        testCourse.setName(Util.getRandomString());
        testCourse.setWeight(Util.getRandomFloat());
        testCourse.setCost(Util.getRandomFloat());
        testCourse.setCourseCategory(courseCategoryDao.findCourseCategoryById(courseCategoryId()));
        testCourse = courseDao.addCourse(testCourse);

        CookedCourse cookedCourse = cookedCourseDao.addCookedCourse(testCourse, employee(),
                Util.getRandomFloat());

        for (CookedCourse cC : cookedCourseDao.findAllCookedCourses()) {
            System.out.println(cC.getCourse().getName() + " : " + cC.getCookDatetime());
        }

        cookedCourseDao.delCookedCourse(cookedCourse);
        courseDao.delCourse(testCourse);
    }

    @Test(timeout = 20000)
    public void addFindDelWarehouseTest() throws Exception {
        for (Ingredient ingredient: ingredientDao.findAllIngredients()) {
            for (Portion portion : portionDao.findAllPortions()) {
                float amountToAdd = Util.getRandomFloat();
                warehouseDao.addIngredientToWarehouse(ingredient, portion, amountToAdd);
                float amountToTake = Util.getRandomFloat();
                warehouseDao.takeIngredientFromWarehouse(ingredient, portion, amountToTake);

                System.out.println("portionDao.findPortionById(" + portion.getPortionId() + ") test ...");
                assertTrue(portion.equals(portionDao.findPortionById(portion.getPortionId())));

                // "Clear" warehouse position
                warehouseDao.takeIngredientFromWarehouse(ingredient, portion, amountToAdd - amountToTake);
            }

            System.out.println("ingredientDao.findIngredientById(" + ingredient.getId() + ") test ...");
            assertTrue(ingredient.equals(ingredientDao.findIngredientById(ingredient.getId())));

            System.out.println("Warehouse: " + ingredient.getName() + " : ");
            for (Warehouse warehouse : warehouseDao.findIngredientInWarehouseByName(ingredient.getName())) {
                System.out.println(warehouse.getPortion().getDescription() + ": " + warehouse.getAmount());
            }
        }

        System.out.println("Warehouse all ingredients:");
        for (Warehouse warehouse : warehouseDao.findAllWarehouseIngredients()) {
            System.out.println(warehouse.getIngredient().getName() + ": " + warehouse.getAmount());
        }
        System.out.println("Warehouse elapsing ingredients:");
        for (Warehouse warehouse : warehouseDao.findAllElapsingWarehouseIngredients((float)500.0)) {
            System.out.println(warehouse.getIngredient().getName() + ": " +
                    warehouse.getPortion().getDescription() + ": " +
                    warehouse.getAmount());
        }
    }
}
