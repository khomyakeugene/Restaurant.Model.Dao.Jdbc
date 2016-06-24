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
    private static CookedCourseViewDao cookedCourseViewDao;
    private static StateGraphDao stateGraphDao;
    private static OrderDao orderDao;
    private static IngredientDao ingredientDao;
    private static PortionDao portionDao;
    private static WarehouseViewDao warehouseViewDao;

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
        cookedCourseViewDao = applicationContext.getBean(CookedCourseViewDao.class);
        stateGraphDao = applicationContext.getBean(StateGraphDao.class);
        orderDao = applicationContext.getBean(OrderDao.class);
        ingredientDao = applicationContext.getBean(IngredientDao.class);
        portionDao = applicationContext.getBean(PortionDao.class);
        warehouseViewDao = applicationContext.getBean(WarehouseViewDao.class);
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
        employee.setJobPositionId(jobPositionId());
        employee.setFirstName(firstName);
        employee.setSecondName(secondName);
        employee.setPhoneNumber(Util.getRandomString());
        employee.setSalary(Util.getRandomFloat());

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
        String name = Util.getRandomString();
        Course course = new Course();
        course.setCategoryId(courseCategoryId());
        course.setName(name);
        course.setWeight(Util.getRandomFloat());
        course.setCost(Util.getRandomFloat());
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

        for (MenuCourseView menuCourseList : menuDao.findMenuCourses(menu)) {
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
        order.setTableId(tableId());
        order.setEmployeeId(employeeId());
        order.setOrderNumber(Util.getRandomString());
        order.setStateType("A");
        order = orderDao.addOrder(order);
        int orderId = order.getOrderId();

        assertTrue(order.equals(orderDao.findOrderById(order.getOrderId())));

        // Courses in orderView ----------------------------
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

        orderDao.addCourseToOrder(order, course1);
        orderDao.addCourseToOrder(order, course2);

        for (Course course : orderDao.findAllOrderCourses(order)) {
            orderDao.findOrderCourseByCourseId(order, course.getCourseId());
            System.out.println(course.getName() + " : " + course.getCost());
        }

        orderDao.takeCourseFromOrder(order, course1);
        orderDao.takeCourseFromOrder(order, course1);
        orderDao.takeCourseFromOrder(order, course2);

        courseDao.delCourse(courseName1);
        courseDao.delCourse(courseName2);
        // ----------------------------

        for (Order o : orderDao.findAllOrders()) {
            System.out.println("Order id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        for (Order o : orderDao.findAllOrders("A")) {
            System.out.println("Open orderView id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        for (Order o : orderDao.findAllOrders("B")) {
            System.out.println("Closed orderView id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
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
        testCourse.setCategoryId(courseCategoryId());
        testCourse.setName(Util.getRandomString());
        testCourse.setWeight(Util.getRandomFloat());
        testCourse.setCost(Util.getRandomFloat());

        testCourse = courseDao.addCourse(testCourse);

        CookedCourseView cookedCourseView = cookedCourseViewDao.addCookedCourse(testCourse, employee(),
                Util.getRandomFloat());

        for (CookedCourseView cCV : cookedCourseViewDao.findAllCookedCourses()) {
            System.out.println(cCV.getCourseName() + " : " + cCV.getCookDatetime());
        }

        cookedCourseViewDao.delCookedCourse(cookedCourseView);
        courseDao.delCourse(testCourse);
    }

    @Test(timeout = 10000)
    public void addFindDelWarehouseTest() throws Exception {
        for (Ingredient ingredient: ingredientDao.findAllIngredients()) {
            for (Portion portion : portionDao.findAllPortions()) {
                float amountToAdd = Util.getRandomFloat();
                warehouseViewDao.addIngredientToWarehouse(ingredient, portion, amountToAdd);
                float amountToTake = Util.getRandomFloat();
                warehouseViewDao.takeIngredientFromWarehouse(ingredient, portion, amountToTake);

                System.out.println("portionDao.findPortionById(" + portion.getPortionId() + ") test ...");
                assertTrue(portion.equals(portionDao.findPortionById(portion.getPortionId())));

                // "Clear" warehouse position
                warehouseViewDao.takeIngredientFromWarehouse(ingredient, portion, amountToAdd - amountToTake);
            }

            System.out.println("ingredientDao.findIngredientById(" + ingredient.getId() + ") test ...");
            assertTrue(ingredient.equals(ingredientDao.findIngredientById(ingredient.getId())));

            System.out.println("Warehouse: " + ingredient.getName() + " : ");
            for (WarehouseView warehouseView : warehouseViewDao.findIngredientInWarehouseByName(ingredient.getName())) {
                System.out.println(warehouseView.getPortionDescription() + ": " + warehouseView.getAmount());
            }
        }

        System.out.println("Warehouse all ingredients:");
        for (WarehouseView warehouseView : warehouseViewDao.findAllWarehouseIngredients()) {
            System.out.println(warehouseView.getIngredientName() + ": " + warehouseView.getAmount());
        }
        System.out.println("Warehouse elapsing ingredients:");
        for (WarehouseView warehouseView : warehouseViewDao.findAllElapsingWarehouseIngredients((float)500.0)) {
            System.out.println(warehouseView.getIngredientName() + ": " + warehouseView.getPortionDescription() + ": " +
                    warehouseView.getAmount());
        }
    }
}
