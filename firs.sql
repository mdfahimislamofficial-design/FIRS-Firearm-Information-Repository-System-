-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 07, 2026 at 06:49 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `firs`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE `cart_items` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(11) DEFAULT 1,
  `added_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`id`, `user_id`, `product_id`, `quantity`, `added_at`) VALUES
(1, 1, 1, 2, '2026-04-07 16:37:52'),
(2, 1, 9, 1, '2026-04-07 16:37:52'),
(3, 2, 5, 3, '2026-04-07 16:37:52'),
(4, 3, 7, 1, '2026-04-07 16:37:52');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL,
  `order_number` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `total_amount` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `shipping_address` text DEFAULT NULL,
  `ffl_dealer_id` bigint(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `order_number`, `user_id`, `total_amount`, `status`, `payment_status`, `shipping_address`, `ffl_dealer_id`, `created_at`, `updated_at`) VALUES
(1, 'ORD-2026-001', 1, 1797, 'DELIVERED', 'PAID', NULL, NULL, '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(2, 'ORD-2026-002', 2, 4347, 'PROCESSING', 'PAID', NULL, NULL, '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(3, 'ORD-2026-003', 3, 7500, 'PENDING', 'PENDING', NULL, NULL, '2026-04-07 16:37:52', '2026-04-07 16:37:52');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `quantity`, `price`) VALUES
(1, 1, 1, 2, 649),
(2, 1, 9, 1, 499),
(3, 2, 5, 3, 1449),
(4, 3, 7, 1, 7500);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` bigint(20) NOT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `type` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `stock` int(11) DEFAULT 0,
  `caliber` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `sku`, `name`, `price`, `type`, `image`, `description`, `stock`, `caliber`, `manufacturer`, `created_at`, `updated_at`) VALUES
(1, 'GLK-19-G5', 'Glock 19 Gen5', 649, 'HANDGUN', '🔫', 'The Glock 19 Gen5 is a compact, semi-automatic pistol chambered in 9mm. Features include a match-grade Glock Marksman Barrel, improved grip texture, and no finger grooves for enhanced ergonomics.', 84, '9mm', 'Glock', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(2, 'SIG-P320-C', 'Sig Sauer P320 Compact', 799, 'HANDGUN', '🔫', 'The P320 Compact features a modular design allowing the serialized fire control unit to be transferred to different grip modules and calibers. Includes night sights and 15-round capacity.', 62, '9mm', 'Sig Sauer', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(3, 'S&W-MP40', 'Smith & Wesson M&P40', 549, 'HANDGUN', '🔫', 'The M&P40 delivers reliability and performance with its proven striker-fired action. Features include an 4.25\" barrel, 15+1 capacity, and aggressive grip texture.', 45, '.40 S&W', 'Smith & Wesson', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(4, 'RUG-GP100', 'Ruger GP100 Revolver', 749, 'REVOLVER', '🎯', 'The GP100 is a rugged, reliable double-action revolver chambered in .357 Magnum. Features a full-lug barrel, adjustable sights, and triple-locking cylinder.', 18, '.357 Mag', 'Ruger', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(5, 'BCM-RECCE16', 'BCM RECCE-16 MCMR', 1449, 'RIFLE', '🔫', 'The RECCE-16 features a 16\" cold hammer-forged barrel, MCMR handguard, and BCMs proven bolt carrier group. Perfect for tactical and sporting applications.', 34, '5.56 NATO', 'BCM', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(6, 'DD-DDM4V7', 'Daniel Defense DDM4 V7', 2099, 'RIFLE', '🔫', 'The DDM4 V7 features Daniel Defense RIS III M-LOK rail system, Cold Hammer Forged barrel, and their proprietary bolt carrier group. Battle-proven reliability.', 7, '5.56 NATO', 'Daniel Defense', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(7, 'AI-AXMC', 'Accuracy International AXMC', 7500, 'SNIPER', '🎯', 'The AXMC is a multi-caliber sniper system chambered in .338 Lapua Magnum. Features a quick-change barrel system, folding stock, and sub-MOA accuracy guarantee.', 4, '.338 Lapua', 'Accuracy International', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(8, 'SAV-110-EP', 'Savage 110 Elite Precision', 1699, 'SNIPER', '🎯', 'The 110 Elite Precision features an MDT ACC chassis system, 26\" heavy fluted barrel, and AccuTrigger. Designed for long-range precision shooting.', 2, '6.5 Creedmoor', 'Savage', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(9, 'CZ-P10C', 'CZ P-10 C', 499, 'HANDGUN', '🔫', 'The P-10 C is a striker-fired pistol with excellent ergonomics, low bore axis, and crisp trigger pull. Features 15+1 capacity and interchangeable backstraps.', 56, '9mm', 'CZ', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(10, 'SW-686P', 'Smith & Wesson 686+', 829, 'REVOLVER', '🎯', 'The 686 Plus features a 7-shot cylinder, full-lug barrel, and adjustable rear sight. Chambered in .357 Magnum with smooth trigger pull.', 29, '.357 Mag', 'Smith & Wesson', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(11, 'ARSL-SLR107', 'Arsenal SLR-107FR', 1149, 'RIFLE', '🔫', 'Bulgarian-made AK-47 variant with milled receiver, side-folding stock, and military-grade finish. Chambered in 7.62x39mm.', 23, '7.62x39mm', 'Arsenal', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(12, 'REM-700-SPS', 'Remington 700 SPS Tactical', 849, 'SNIPER', '🎯', 'The 700 SPS Tactical features a 20\" heavy-contour barrel, Hogue overmolded stock, and X-Mark Pro trigger. Sub-MOA accuracy out of the box.', 15, '.308 Win', 'Remington', '2026-04-07 16:37:52', '2026-04-07 16:37:52');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `dob` varchar(255) DEFAULT NULL,
  `nid` varchar(255) DEFAULT NULL,
  `comment` text DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `badge_number` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `ffl_number` varchar(255) DEFAULT NULL,
  `business_name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `dob`, `nid`, `comment`, `role`, `badge_number`, `department`, `ffl_number`, `business_name`, `status`, `created_at`, `updated_at`) VALUES
(1, 'John Customer', 'customer@example.com', 'customer123', NULL, NULL, NULL, 'CUSTOMER', NULL, NULL, NULL, NULL, 'APPROVED', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(2, 'Mike Dealer', 'dealer@example.com', 'dealer123', NULL, NULL, NULL, 'DEALER', NULL, NULL, NULL, NULL, 'APPROVED', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(3, 'Lt. Holloway', 'gov@example.com', 'gov123', NULL, NULL, NULL, 'GOVERNMENT', NULL, NULL, NULL, NULL, 'APPROVED', '2026-04-07 16:37:52', '2026-04-07 16:37:52'),
(4, 'Admin User', 'admin@example.com', 'admin123', NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL, NULL, 'APPROVED', '2026-04-07 16:37:52', '2026-04-07 16:37:52');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `order_number` (`order_number`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_status` (`status`),
  ADD KEY `idx_order_number` (`order_number`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `idx_order_id` (`order_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sku` (`sku`),
  ADD KEY `idx_type` (`type`),
  ADD KEY `idx_manufacturer` (`manufacturer`),
  ADD KEY `idx_caliber` (`caliber`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_role` (`role`),
  ADD KEY `idx_status` (`status`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
