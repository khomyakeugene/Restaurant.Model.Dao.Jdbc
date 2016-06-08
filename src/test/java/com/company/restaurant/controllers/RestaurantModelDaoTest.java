package com.company.restaurant.controllers;

import com.company.restaurant.dao.*;
import com.company.restaurant.model.*;
import com.company.util.ObjectService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Yevhen on 20.05.2016.
 */
public class RestaurantModelDaoTest {
    private final static String APPLICATION_CONTEXT_NAME = "restaurant-jdbc-context.xml";

    private static JobPositionDao jobPositionDao;
    private static EmployeeDao employeeDao;
    private static MenuDao menuDao;
    private static TableDao tableDao;
    private static CourseDao courseDao;
    private static CourseCategoryDao courseCategoryDao;
    private static CookedCourseDao cookedCourseDao;
    private static OrderDao orderDao;
    private static OrderCourseDao orderCourseDao;
    private static IngredientDao ingredientDao;
    private static PortionDao portionDao;
    private static WarehouseDao warehouseDao;

    private static int closedOrderId;
    private static Order closedOrder;
    private static String closedOrderCourseName1;
    private static Course closedOrderCourse1;
    private static String closedOrderCourseName2;
    private static Course closedOrderCourse2;
    private static Course testCourse;

    private static Employee employee() {
        return employeeDao.findAllEmployees().get(0);
    }

    private int jobPositionId() {
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

    private static Course prepareTestCourse() {
        testCourse = new Course();
        testCourse.setCategoryId(courseCategoryId());
        testCourse.setName(Util.getRandomString());
        testCourse.setWeight(Util.getRandomFloat());
        testCourse.setCost(Util.getRandomFloat());

        testCourse = courseDao.addCourse(testCourse);

        return testCourse;
    }

    private static void delTestCourse() {
        courseDao.delCourse(testCourse);
    }

    private static void prepareClosedOrder() throws Exception {
        Order order = new Order();
        order.setTableId(tableId());
        order.setEmployeeId(employeeId());
        order.setOrderNumber(Util.getRandomString());
        order.setStateType("A");
        closedOrderId = orderDao.addOrder(order).getOrderId();

        // Courses for closed order ----------------------------
        closedOrderCourseName1 = Util.getRandomString();
        closedOrderCourse1 = new Course();
        closedOrderCourse1.setCategoryId(courseCategoryId());
        closedOrderCourse1.setName(closedOrderCourseName1);
        closedOrderCourse1.setWeight(Util.getRandomFloat());
        closedOrderCourse1.setCost(Util.getRandomFloat());
        closedOrderCourse1 = courseDao.addCourse(closedOrderCourse1);

        closedOrderCourseName2 = Util.getRandomString();
        closedOrderCourse2 = new Course();
        closedOrderCourse2.setCategoryId(courseCategoryId());
        closedOrderCourse2.setName(closedOrderCourseName2);
        closedOrderCourse2.setWeight(Util.getRandomFloat());
        closedOrderCourse2.setCost(Util.getRandomFloat());
        closedOrderCourse2 = courseDao.addCourse(closedOrderCourse2);
        // ----------

        orderCourseDao.addCourseToOrder(order, closedOrderCourse1, 1);

        closedOrder = orderDao.updOrderState(order, "B");
    }

    private static void clearClosedOrder() throws Exception {
        Order order = orderDao.findOrderById(closedOrderId);
        if (order != null) {
            // Manually change order state to "open"
            order = orderDao.updOrderState(order, "A");
            // Delete "open" order
            orderDao.delOrder(order);
        }

        // Delete course for closed order
        courseDao.delCourse(closedOrderCourseName1);
        courseDao.delCourse(closedOrderCourseName2);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_NAME);

        menuDao = applicationContext.getBean(MenuDao.class);
        tableDao = applicationContext.getBean(TableDao.class);
        employeeDao = applicationContext.getBean(EmployeeDao.class);
        jobPositionDao = applicationContext.getBean(JobPositionDao.class);
        courseDao = applicationContext.getBean(CourseDao.class);
        courseCategoryDao = applicationContext.getBean(CourseCategoryDao.class);
        cookedCourseDao = applicationContext.getBean(CookedCourseDao.class);
        orderDao = applicationContext.getBean(OrderDao.class);
        orderCourseDao = applicationContext.getBean(OrderCourseDao.class);
        ingredientDao = applicationContext.getBean(IngredientDao.class);
        portionDao = applicationContext.getBean(PortionDao.class);
        warehouseDao = applicationContext.getBean(WarehouseDao.class);

        prepareTestCourse();
        prepareClosedOrder();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        delTestCourse();
        clearClosedOrder();

        System.out.println("tearDownClass finished!");
    }

    @Test(timeout = 2000)
    public void addFindDelJobPosition() throws Exception {
        String name = Util.getRandomString();
        JobPosition jobPosition = jobPositionDao.addJobPosition(name);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(jobPosition,
                jobPositionDao.findJobPositionByName(jobPosition.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(jobPosition,
                jobPositionDao.findJobPositionById(jobPosition.getId())));

        jobPositionDao.delJobPosition(name);
        assertTrue(jobPositionDao.findJobPositionByName(name) == null);
        // Test delete of non-existent data
        jobPositionDao.delJobPosition(name);
    }

    @Test//(timeout = 2000)
    public void addFindDelEmployeeTest() throws Exception {
        for (JobPosition jobPosition : jobPositionDao.findAllJobPositions()) {
            System.out.println("Job position Id :" + jobPosition.getId() +
                    ", Job position name :" + jobPosition.getName());
        }


        String firstName = Util.getRandomString();
        String secondName = Util.getRandomString();
        Employee employee = new Employee();
        employee.setJobPositionId(jobPositionId());
        employee.setFirstName(firstName);
        employee.setSecondName(secondName);
        employee.setPhoneNumber(Util.getRandomString());
        employee.setSalary(Util.getRandomFloat());

        employee = employeeDao.addEmployee(employee);
        int employeeId = employee.getEmployeeId();

        // Select test <employee> and check
        Employee employeeByFirstName = employeeDao.findEmployeeByFirstName(firstName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeByFirstName));

        Employee employeeBySecondName = employeeDao.findEmployeeBySecondName(secondName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeBySecondName));

        Employee employeeByFirstAndSecondName =
                employeeDao.findEmployeeByFirstAndSecondName(firstName, secondName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeByFirstAndSecondName));

        Employee employeeById = employeeDao.findEmployeeById(employeeId);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeById));

        employeeDao.delEmployee(employee);
        assertTrue(employeeDao.findEmployeeById(employeeId) == null);
        // Test delete of non-existent data
        employeeDao.delEmployee(employee);
        employeeDao.delEmployee(employee.getEmployeeId());
    }

    @Test(timeout = 2000)
    public void addFindDelCourseCategoryTest() throws Exception {
        String name = Util.getRandomString();
        CourseCategory courseCategory = courseCategoryDao.addCourseCategory(name);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(courseCategory,
                courseCategoryDao.findCourseCategoryByName(courseCategory.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(courseCategory,
                courseCategoryDao.findCourseCategoryById(courseCategory.getId())));

        courseCategoryDao.delCourseCategory(name);
        assertTrue(courseCategoryDao.findCourseCategoryByName(name) == null);
        // Test delete of non-existent data
        courseCategoryDao.delCourseCategory(name);
    }

    @Test(timeout = 2000)
    public void addFindDelCourseTest() throws Exception {
        String name = Util.getRandomString();
        Course course = new Course();
        course.setCategoryId(courseCategoryId());
        course.setName(name);
        course.setWeight(Util.getRandomFloat());
        course.setCost(Util.getRandomFloat());
        course = courseDao.addCourse(course);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseDao.findCourseByName(course.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseDao.findCourseById(course.getCourseId())));

        courseDao.delCourse(name);
        assertTrue(courseDao.findCourseByName(name) == null);
        // Test delete by "the whole object"
        course = courseDao.addCourse(course);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseDao.findCourseByName(name)));
        courseDao.delCourse(course);
        assertTrue(courseDao.findCourseByName(name) == null);
        // Test delete of non-existent data
        courseDao.delCourse(name);

        // Whole course list
        courseDao.findAllCourses();
    }

    @Test(timeout = 2000)
    public void addFindDelMenuTest() throws Exception {
        String name = Util.getRandomString();
        Menu menu = menuDao.addMenu(name);

        Menu menuByName = menuDao.findMenuByName(name);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(menu, menuByName));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(menu,
                menuDao.findMenuById(menu.getId())));

        // Courses in menu ----------------------------
        String courseName1 = Util.getRandomString();
        Course course1 = new Course();
        course1.setCategoryId(courseCategoryId());
        course1.setName(courseName1);
        course1.setWeight(Util.getRandomFloat());
        course1.setCost(Util.getRandomFloat());
        course1 = courseDao.addCourse(course1);

        String courseName2 = Util.getRandomString();
        Course course2 = new Course();
        course2.setCategoryId(courseCategoryId());
        course2.setName(courseName2);
        course2.setWeight(Util.getRandomFloat());
        course2.setCost(Util.getRandomFloat());
        course2 = courseDao.addCourse(course2);

        menuDao.addCourseToMenu(menu, course1);
        menuDao.addCourseToMenu(menu, course2);

        for (MenuCourseList menuCourseList : menuDao.findMenuCourses(menu)) {
            menuDao.findMenuCourseByCourseId(menu, menuCourseList.getCourseId());
            System.out.println(menuCourseList.getCourseName() + ": " + menuCourseList.getCourseCategoryName());
        }


        menuDao.delCourseFromMenu(menu, course1);
        menuDao.delCourseFromMenu(menu, course2);

        courseDao.delCourse(courseName1);
        courseDao.delCourse(courseName2);
        // ----------------------------

        for (Menu m : menuDao.findAllMenus()) {
            System.out.println("menu_id: " + m.getId() + ", name: " + m.getName());
        }

        menuDao.delMenu(name);
        assertTrue(menuDao.findMenuByName(name) == null);
        // Test delete of non-existent data
        menuDao.delMenu(menu);
    }

    @Test(timeout = 2000)
    public void addFindDelTableTest() throws Exception {
        Table table = new Table();
        table.setNumber(tableDao.findTableById(lastTableId()).getNumber() + Util.getRandomInteger());
        table.setDescription(Util.getRandomString());
        table = tableDao.addTable(table);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(table,
                tableDao.findTableByNumber(table.getNumber())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(table,
                tableDao.findTableById(table.getTableId())));

        tableDao.delTable(table);
        assertTrue(tableDao.findTableByNumber(table.getNumber()) == null);
    }

    @Test (timeout = 2000)
    public void addFindDelOrderTest() throws Exception {
        Order order = new Order();
        order.setTableId(tableId());
        order.setEmployeeId(employeeId());
        order.setOrderNumber(Util.getRandomString());
        order.setStateType("A");
        int orderId = orderDao.addOrder(order).getOrderId();

        Order orderById = orderDao.findOrderById(orderId);
        // Just check of successful retrieving from database,  without "full comparing"!!!
        // Because, at least field <order_datetime> is filling by default (as a current timestamp) on the database level
        assertTrue(orderById != null);

        // Courses in order ----------------------------
        String courseName1 = Util.getRandomString();
        Course course1 = new Course();
        course1.setCategoryId(courseCategoryId());
        course1.setName(courseName1);
        course1.setWeight(Util.getRandomFloat());
        course1.setCost(Util.getRandomFloat());
        course1 = courseDao.addCourse(course1);

        String courseName2 = Util.getRandomString();
        Course course2 = new Course();
        course2.setCategoryId(courseCategoryId());
        course2.setName(courseName2);
        course2.setWeight(Util.getRandomFloat());
        course2.setCost(Util.getRandomFloat());
        course2 = courseDao.addCourse(course2);

        orderCourseDao.addCourseToOrder(order, course1, 3);
        orderCourseDao.addCourseToOrder(order, course2, 2);

        for (OrderCourse orderCourse : orderCourseDao.findAllOrderCourses(order)) {
            orderCourseDao.findOrderCourseByCourseId(order, orderCourse.getCourseId());
            System.out.println(orderCourse.getCourseName() + " : " + orderCourse.getCourseCost());
        }

        orderCourseDao.takeCourseFromOrder(order, course1, 2);
        orderCourseDao.takeCourseFromOrder(order, course1, 1);
        orderCourseDao.takeCourseFromOrder(order, course2, 2);

        courseDao.delCourse(courseName1);
        courseDao.delCourse(courseName2);
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
    }

    @Test(timeout = 2000)
    public void closedOrderTest_1() throws Exception {
        orderDao.delOrder(closedOrder);
    }

    @Test(timeout = 2000)
    public void closedOrderTest_2() throws Exception {
        orderCourseDao.addCourseToOrder(closedOrder, closedOrderCourse2, 1);
    }

    @Test(timeout = 2000)
    public void closedOrderTest_3() throws Exception {
        orderCourseDao.takeCourseFromOrder(closedOrder, closedOrderCourse1, 1);
    }

    @Test(timeout = 2000)
    public void addCookedCourse() throws Exception {
        cookedCourseDao.addCookedCourse(testCourse, employee(), Util.getRandomFloat());

        for (CookedCourse cookedCourse : cookedCourseDao.findAllCookedCourses()) {
            System.out.println(cookedCourse.getCourseName() + " : " + cookedCourse.getCookDatetime());
        }
    }

    @Test(timeout = 10000)
    public void addFindDelWarehouseTest() throws Exception {
        for (Ingredient ingredient: ingredientDao.findAllIngredients()) {
            for (Portion portion : portionDao.findAllPortions()) {
                warehouseDao.addIngredientToWarehouse(ingredient, portion, Util.getRandomFloat());
                warehouseDao.takeIngredientFromWarehouse(ingredient, portion, Util.getRandomFloat());

                System.out.println("menuController.findPortionById(" + portion.getPortionId() + ") test ...");
                assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(portion,
                        portionDao.findPortionById(portion.getPortionId())));
            }

            System.out.println("menuController.findIngredientById(" + ingredient.getId() + ") test ...");
            assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(ingredient,
                    ingredientDao.findIngredientById(ingredient.getId())));

            System.out.println("Warehouse: " + ingredient.getName() + " : ");
            for (Warehouse warehouse : warehouseDao.findIngredientInWarehouseByName(ingredient.getName())) {
                System.out.println(warehouse.getPortionDescription() + ": " + warehouse.getAmount());
            }
        }

        System.out.println("Warehouse all ingredients:");
        for (Warehouse warehouse : warehouseDao.findAllWarehouseIngredients()) {
            System.out.println(warehouse.getIngredientName() + ": " + warehouse.getAmount());
        }
        System.out.println("Warehouse elapsing ingredients:");
        for (Warehouse warehouse : warehouseDao.findAllElapsingWarehouseIngredients((float)500.0)) {
            System.out.println(warehouse.getIngredientName() + ": " + warehouse.getPortionDescription() + ": " +
                    warehouse.getAmount());
        }
    }
}